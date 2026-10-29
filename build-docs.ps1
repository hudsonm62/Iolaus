# Build Kt Dokka HTML documentation
$gw = Join-Path $PSScriptRoot $(if (-not $IsWindows) { "gradlew" } else { "gradlew.bat" })
& $gw ":common:dokkaGenerateHtml"

# Build Zensical site
zensical build --clean

# Copy Dokka into /api of Zensical site
Copy-Item -Path (Join-Path $PSScriptRoot "common/build/dokka/html") -Destination "build/site/api" -Recurse -Force -ErrorAction Stop
