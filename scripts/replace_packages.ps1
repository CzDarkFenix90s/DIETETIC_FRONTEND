$path = 'C:\Users\alexi\DIETETIC_BACKEND\proyect_idiomas_danny\app\src\main\java'
Get-ChildItem -Path $path -Recurse -Include *.kt,*.java | ForEach-Object {
    $file = $_.FullName
    $content = Get-Content -Raw -Encoding UTF8 $file
    $new = $content -replace 'com\\.ute\\.guamanidiomas','com.dietetic.frontend'
    if ($new -ne $content) {
        Set-Content -Encoding UTF8 $file -Value $new
        Write-Output "Updated: $file"
    }
}
Write-Output "Replacement complete."