# build_android.ps1

$projectDir = (Get-Location).Path
Write-Host "Démarrage du build Android avec Docker..."

docker run --rm -v "${projectDir}:/app" -w /app build-android ./gradlew assembleDebug --info

if ($LASTEXITCODE -eq 0) {
    Write-Host "Build terminé avec succès !"
    $apkPath = Join-Path $projectDir "app\build\outputs\apk\debug\app-debug.apk"
    if (Test-Path $apkPath) {
        Write-Host "APK généré : $apkPath"
        # Ouvre le dossier contenant l’APK
        Invoke-Item (Split-Path $apkPath)
    }
    else {
        Write-Host "APK non trouvé. Vérifie le build."
    }
}
else {
    Write-Host "Erreur pendant le build."
}
