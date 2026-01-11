# Release script for rivalchess-uci
# Usage: .\release.ps1 <version>
# Example: .\release.ps1 37.0.0

param(
    [Parameter(Mandatory=$true)]
    [string]$Version
)

$ErrorActionPreference = "Stop"

# Validate version format (x.x.x)
if ($Version -notmatch '^\d+\.\d+\.\d+$') {
    Write-Error "Error: Version must be in format x.x.x (e.g., 37.0.0)"
    exit 1
}

Write-Host "Releasing version: v$Version"

# Check for uncommitted changes
$gitStatus = git status --porcelain
if ($gitStatus) {
    Write-Error "Error: You have uncommitted changes. Please commit or stash them first."
    exit 1
}

# Update build.gradle
$buildGradle = Get-Content "build.gradle" -Raw
$buildGradle = $buildGradle -replace "def uci_version = '[^']*'", "def uci_version = '$Version'"
Set-Content "build.gradle" $buildGradle -NoNewline

Write-Host "Updated build.gradle"

# Verify the build works
Write-Host "Building to verify..."
& .\gradlew.bat build
if ($LASTEXITCODE -ne 0) {
    Write-Error "Build failed!"
    exit 1
}

# Commit the changes
git add build.gradle
git commit -m "Release v$Version"

# Create tag
git tag "v$Version"

Write-Host ""
Write-Host "Release prepared successfully!"
Write-Host "  - build.gradle updated"
Write-Host "  - Commit created"
Write-Host "  - Tag v$Version created"
Write-Host ""
Write-Host "To publish the release, run:"
Write-Host "  git push; git push --tags"
Write-Host ""
Write-Host "The GitHub Action will automatically build and create a release."
