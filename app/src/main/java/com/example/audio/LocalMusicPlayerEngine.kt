package com.example.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MusicTrack(
    val id: String,
    val title: String,
    val artist: String = "Local Audio",
    val uri: Uri? = null,
    val durationMs: Long = 0,
    val isFavorite: Boolean = false
)

enum class RepeatMode {
    OFF, ALL, ONE
}

data class PlayerState(
    val isPlaying: Boolean = false,
    val currentTrack: MusicTrack? = null,
    val currentPositionMs: Long = 0,
    val durationMs: Long = 0,
    val playbackSpeed: Float = 1.0f,
    val isShuffle: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val volume: Float = 1.0f,
    val sleepTimerMinutesLeft: Int? = null,
    val isSoundscapeLayeringActive: Boolean = true
)

class LocalMusicPlayerEngine(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var progressJob: Job? = null
    private var sleepTimerJob: Job? = null

    val binauralEngine = BinauralAudioEngine()

    private val _playlist = MutableStateFlow<List<MusicTrack>>(
        listOf(
            MusicTrack("default_1", "Deep Alpha Focus Stream", "Aura Ambient Lab", null, 180000),
            MusicTrack("default_2", "Theta Frequency Meditation", "Binaural Mindscapes", null, 240000),
            MusicTrack("default_3", "Gamma Peak Cognition Pulse", "NeuroFlow Audio", null, 210000)
        )
    )
    val playlist: StateFlow<List<MusicTrack>> = _playlist.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState(currentTrack = _playlist.value.firstOrNull()))
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var currentIndex = 0

    init {
        startProgressLoop()
    }

    private fun startProgressLoop() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive) {
                delay(500)
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        _playerState.value = _playerState.value.copy(
                            currentPositionMs = player.currentPosition.toLong(),
                            durationMs = player.duration.toLong().coerceAtLeast(1)
                        )
                    }
                }
            }
        }
    }

    fun addTrack(track: MusicTrack) {
        val updated = _playlist.value + track
        _playlist.value = updated
        if (_playerState.value.currentTrack == null) {
            playTrack(track)
        }
    }

    fun scanDeviceAudio(context: Context) {
        scope.launch(Dispatchers.IO) {
            val scanner = MediaStoreAudioScanner(context)
            val songs = scanner.scanDeviceAudioFiles()
            if (songs.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    val currentIds = _playlist.value.map { it.id }.toSet()
                    val newTracks = songs.filter { it.id !in currentIds }
                    if (newTracks.isNotEmpty()) {
                        _playlist.value = _playlist.value + newTracks
                        if (_playerState.value.currentTrack == null) {
                            playTrack(newTracks.first())
                        }
                    }
                }
            }
        }
    }

    fun playTrack(track: MusicTrack) {
        val index = _playlist.value.indexOfFirst { it.id == track.id }
        if (index >= 0) currentIndex = index

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        try {
            if (track.uri != null) {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(context, track.uri)
                    prepare()
                    setVolume(_playerState.value.volume, _playerState.value.volume)
                    setOnCompletionListener { onTrackCompleted() }
                    start()
                }
            } else {
                // Synthesized sample track: Start Binaural engine
                binauralEngine.start()
            }

            _playerState.value = _playerState.value.copy(
                isPlaying = true,
                currentTrack = track,
                currentPositionMs = 0,
                durationMs = track.durationMs
            )
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to binaural tone
            binauralEngine.start()
            _playerState.value = _playerState.value.copy(
                isPlaying = true,
                currentTrack = track
            )
        }
    }

    fun togglePlayPause() {
        val curTrack = _playerState.value.currentTrack
        if (curTrack == null && _playlist.value.isNotEmpty()) {
            playTrack(_playlist.value.first())
            return
        }

        if (_playerState.value.isPlaying) {
            mediaPlayer?.pause()
            binauralEngine.stop()
            _playerState.value = _playerState.value.copy(isPlaying = false)
        } else {
            if (mediaPlayer != null) {
                mediaPlayer?.start()
            } else if (curTrack != null) {
                playTrack(curTrack)
                return
            }
            if (_playerState.value.isSoundscapeLayeringActive) {
                binauralEngine.start()
            }
            _playerState.value = _playerState.value.copy(isPlaying = true)
        }
    }

    fun seekTo(positionMs: Long) {
        mediaPlayer?.seekTo(positionMs.toInt())
        _playerState.value = _playerState.value.copy(currentPositionMs = positionMs)
    }

    fun jump(seconds: Int) {
        val newPos = (_playerState.value.currentPositionMs + seconds * 1000)
            .coerceIn(0, _playerState.value.durationMs.coerceAtLeast(1))
        seekTo(newPos)
    }

    fun playNext() {
        val list = _playlist.value
        if (list.isEmpty()) return
        currentIndex = if (_playerState.value.isShuffle) {
            (0 until list.size).random()
        } else {
            (currentIndex + 1) % list.size
        }
        playTrack(list[currentIndex])
    }

    fun playPrevious() {
        val list = _playlist.value
        if (list.isEmpty()) return
        currentIndex = if (currentIndex - 1 < 0) list.size - 1 else currentIndex - 1
        playTrack(list[currentIndex])
    }

    fun toggleShuffle() {
        _playerState.value = _playerState.value.copy(isShuffle = !_playerState.value.isShuffle)
    }

    fun toggleRepeat() {
        val next = when (_playerState.value.repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        _playerState.value = _playerState.value.copy(repeatMode = next)
    }

    fun setPlaybackSpeed(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer?.playbackParams = mediaPlayer?.playbackParams?.setSpeed(speed) ?: return
        }
        _playerState.value = _playerState.value.copy(playbackSpeed = speed)
    }

    fun setVolume(vol: Float) {
        mediaPlayer?.setVolume(vol, vol)
        _playerState.value = _playerState.value.copy(volume = vol)
    }

    fun toggleFavorite(trackId: String) {
        _playlist.value = _playlist.value.map {
            if (it.id == trackId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun setSleepTimer(minutes: Int?) {
        sleepTimerJob?.cancel()
        if (minutes == null || minutes <= 0) {
            _playerState.value = _playerState.value.copy(sleepTimerMinutesLeft = null)
            return
        }

        _playerState.value = _playerState.value.copy(sleepTimerMinutesLeft = minutes)
        sleepTimerJob = scope.launch {
            var mins = minutes
            while (mins > 0 && isActive) {
                delay(60000L)
                mins--
                _playerState.value = _playerState.value.copy(sleepTimerMinutesLeft = mins)
            }
            // Shut off audio when timer fires
            mediaPlayer?.pause()
            binauralEngine.stop()
            _playerState.value = _playerState.value.copy(isPlaying = false, sleepTimerMinutesLeft = null)
        }
    }

    private fun onTrackCompleted() {
        when (_playerState.value.repeatMode) {
            RepeatMode.ONE -> _playerState.value.currentTrack?.let { playTrack(it) }
            RepeatMode.ALL -> playNext()
            RepeatMode.OFF -> {
                if (currentIndex < _playlist.value.size - 1) {
                    playNext()
                } else {
                    _playerState.value = _playerState.value.copy(isPlaying = false)
                    binauralEngine.stop()
                }
            }
        }
    }

    fun release() {
        progressJob?.cancel()
        sleepTimerJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        binauralEngine.stop()
    }
}
