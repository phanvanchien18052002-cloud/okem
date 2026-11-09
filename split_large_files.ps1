# split_large_files.ps1
# Tu dong cat cac file > 90MB thanh cac phan ~70MB de push len GitHub ma khong bi chan.
$ErrorActionPreference = "Stop"

$scriptDir = $PSScriptRoot
if (-not $scriptDir) { $scriptDir = (Get-Location).Path }

$maxSize = 90MB
$chunkSize = 70MB

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "  CHUAN BI FILE DAY LEN GITHUB (CHONG LOI MAT HINH ANH)   " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

$largeFiles = Get-ChildItem -Path $scriptDir -Recurse -File | Where-Object { 
    $_.Length -gt $maxSize -and $_.Name -notmatch '\.part\d+$' -and $_.Name -notmatch '\.original$' -and $_.Name -notmatch '\.bak$' 
}

if ($largeFiles.Count -eq 0) {
    Write-Host "Khong co file nao > 90MB can cat (hoac da duoc cat roi)." -ForegroundColor Green
    exit 0
}

foreach ($file in $largeFiles) {
    $filePath = $file.FullName
    $relPath = $filePath.Substring($scriptDir.Length + 1)
    $fileSizeMB = [math]::Round($file.Length / 1MB, 2)
    
    Write-Host "Dang cat file: $relPath ($fileSizeMB MB)..." -ForegroundColor Yellow
    
    $stream = [System.IO.File]::OpenRead($filePath)
    $buffer = New-Object byte[] $chunkSize
    $partIndex = 0
    
    while ($true) {
        $bytesRead = $stream.Read($buffer, 0, $chunkSize)
        if ($bytesRead -le 0) { break }
        
        $partName = "$filePath.part$("{0:D2}" -f $partIndex)"
        $partStream = [System.IO.File]::Create($partName)
        $partStream.Write($buffer, 0, $bytesRead)
        $partStream.Close()
        
        $partSizeMB = [math]::Round($bytesRead / 1MB, 2)
        Write-Host "  + Da tao: $(Split-Path $partName -Leaf) ($partSizeMB MB)" -ForegroundColor Gray
        $partIndex++
    }
    $stream.Close()
    
    $backupPath = "$filePath.original"
    if (Test-Path $backupPath) { Remove-Item $backupPath -Force }
    Move-Item -Path $filePath -Destination $backupPath -Force
    
    Write-Host "  -> Cat xong $partIndex phan. File goc duoc doi ten thanh .original" -ForegroundColor Green
}

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "HOAN TAT CHUAN BI FILE!" -ForegroundColor Green
Write-Host "San sang de git add, git commit va git push!" -ForegroundColor White
Write-Host "==========================================================" -ForegroundColor Cyan
