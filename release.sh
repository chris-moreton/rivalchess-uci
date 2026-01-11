#!/bin/bash
set -e

# Release script for rivalchess-uci
# Usage: ./release.sh <version>
# Example: ./release.sh 37.0.0

if [ -z "$1" ]; then
    echo "Usage: ./release.sh <version>"
    echo "Example: ./release.sh 37.0.0"
    exit 1
fi

VERSION="$1"

# Validate version format (x.x.x)
if [[ ! "$VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "Error: Version must be in format x.x.x (e.g., 37.0.0)"
    exit 1
fi

echo "Releasing version: v$VERSION"

# Check for uncommitted changes
if ! git diff --quiet || ! git diff --staged --quiet; then
    echo "Error: You have uncommitted changes. Please commit or stash them first."
    exit 1
fi

# Update build.gradle
sed -i "s/def uci_version = '[^']*'/def uci_version = '$VERSION'/" build.gradle

echo "Updated build.gradle"

# Verify the build works
echo "Building to verify..."
./gradlew build

# Commit the changes
git add build.gradle
git commit -m "Release v$VERSION"

# Create tag
git tag "v$VERSION"

echo ""
echo "Release prepared successfully!"
echo "  - build.gradle updated"
echo "  - Commit created"
echo "  - Tag v$VERSION created"
echo ""
echo "To publish the release, run:"
echo "  git push && git push --tags"
echo ""
echo "The GitHub Action will automatically build and create a release."
