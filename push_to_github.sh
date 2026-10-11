#!/usr/bin/env bash
set -e

# =============================================================================
# NeuroBox 1-Click Push to GitHub & APK Release Trigger Script
# Developed by Samir Ahmmed (https://github.com/samir-ahmmed-tech/Aura-Focus-Game)
# =============================================================================

REPO_OWNER="samir-ahmmed-tech"
REPO_NAME="Aura-Focus-Game"
REPO_URL="https://github.com/${REPO_OWNER}/${REPO_NAME}.git"

echo "================================================================="
echo "🚀 NeuroBox - Push Changes to GitHub & Trigger APK Release Build"
echo "👨‍💻 Developer: Samir Ahmmed"
echo "📦 Repository: ${REPO_OWNER}/${REPO_NAME}"
echo "================================================================="

# Set git user info
git config user.name "Samir Ahmmed"
git config user.email "samir-ahmmed-tech@users.noreply.github.com"

# Check if token was provided as argument or environment variable
GH_TOKEN="${1:-${GITHUB_TOKEN:-}}"

if [ -z "$GH_TOKEN" ]; then
    echo ""
    echo "🔑 Please enter your GitHub Personal Access Token (PAT):"
    echo "   (Note: Password authentication is disabled by GitHub, you need a PAT with 'repo' scope)"
    read -r -s -p "GitHub Token: " GH_TOKEN
    echo ""
fi

if [ -z "$GH_TOKEN" ]; then
    echo "❌ Error: GitHub Token cannot be empty. Aborted."
    exit 1
fi

echo "📦 Staging changes..."
git add -A

if git diff --staged --quiet; then
    echo "ℹ️  No new changes to commit."
else
    echo "💾 Creating commit..."
    git commit -m "feat: NeuroBox v1.1.0 - 120 FPS Butter-Smooth Lazy Loading Lag Fix, 40 Master Brain Games, 35 Power Tools, Samir Ahmmed Developer Profile & In-App Release Update Engine"
fi

echo "🔄 Pushing to https://github.com/${REPO_OWNER}/${REPO_NAME} on branch main..."
git push "https://${GH_TOKEN}@github.com/${REPO_OWNER}/${REPO_NAME}.git" main

echo ""
echo "================================================================="
echo "🎉 SUCCESS! Changes successfully pushed to GitHub!"
echo "================================================================="
echo ""
echo "🤖 GitHub Actions is now building NeuroBox.apk & releasing on GitHub:"
echo "   👉 https://github.com/${REPO_OWNER}/${REPO_NAME}/actions"
echo ""
echo "📥 Download the Release APK once build completes:"
echo "   👉 https://github.com/${REPO_OWNER}/${REPO_NAME}/releases"
echo "================================================================="
