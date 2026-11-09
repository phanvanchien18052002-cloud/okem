# restore_large_files.ps1
# Khoi phuc / Ghep lai cac file lon da bi cat (.part) tren Windows.
$ErrorActionPreference = "Stop"

$scriptDir = $PSScriptRoot
if (-not $scriptDir) { $scriptDir = (Get-Location).Path }

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "      KHOI PHUC FILE TU CAC PHAN DA CAT (.part)           " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

$part00Files = Get-ChildItem -Path $scriptDir -Recurse -File -Filter "*.part00"

if ($part00Files.Count -eq 0) {
    $originalFiles = Get-ChildItem -Path $scriptDir -Recurse -File -Filter "*.original"
    if ($originalFiles.Count -gt 0) {
        foreach ($orig in $originalFiles) {
            $dest = $orig.FullName.Substring(0, $orig.FullName.Length - 9)
            Move-Item -Path $orig.FullName -Destination $dest -Force
            Write-Host "Da khoi phuc tu .original: $(Split-Path $dest -Leaf)" -ForegroundColor Green
        }
        Write-Host "Da khoi phuc toan bo file tu ban sao luu .original!" -ForegroundColor Green
        exit 0
    }
    Write-Host "Khong tim thay file .part00 hoac .original nao can ghep." -ForegroundColor Yellow
    exit 0
}

foreach ($p00 in $part00Files) {
    $basePath = $p00.FullName.Substring(0, $p00.FullName.Length - 7) # Bo duoi .part00
    $relPath = $basePath.Substring($scriptDir.Length + 1)
    
    Write-Host "Dang ghep lai: $relPath..." -ForegroundColor Yellow
    $outStream = [System.IO.File]::Create($basePath)
    $partIndex = 0
    
    while ($true) {
        $partName = "$basePath.part$("{0:D2}" -f $partIndex)"
        if (-not (Test-Path $partName)) { break }
        
        $inStream = [System.IO.File]::OpenRead($partName)
        $inStream.CopyTo($outStream)
        $inStream.Close()
        
        Write-Host "  + Da ghep phan $partIndex" -ForegroundColor Gray
        $partIndex++
    }
    $outStream.Close()
    
    $finalSizeMB = [math]::Round((Get-Item $basePath).Length / 1MB, 2)
    Write-Host "  -> Khoi phuc thanh cong: $relPath ($finalSizeMB MB)" -ForegroundColor Green
}

$origFiles = Get-ChildItem -Path $scriptDir -Recurse -File -Filter "*.original"
foreach ($orig in $origFiles) {
    Remove-Item -Path $orig.FullName -Force
}

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "DA KHOI PHUC TOAN BO FILE LON THANH CONG!" -ForegroundColor Green
Write-Host "==========================================================" -ForegroundColor Cyan
