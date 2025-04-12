del signed.apk
java.exe -jar apksigner.jar sign --key testkey.pk8 --cert testkey.x509.pem --out signed.apk mixin.apk
del signed.apk.idsig
