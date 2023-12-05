# How To Patch for billing 

install from https://drive.google.com/drive/folders/1cle84kt7aykYzfCHa0pYXbw0Gbcn2dG7?usp=sharing
## Prerequisite
- modder
- apktool-kotlin
- billing hack apk installed on your device


## installing billing hack server on ur phone

just install the apk from the google drive okay?



## 


download apk first to your computer for patching
```sh
modder download [APK_PACKAGE_NAME]
```

after download, the apk should be installed in [APK_PACKAGE_NAME]
patch the apk, to redirect all purchases to our own server

(need to patch `base.apk`)
```sh
cd [APK_PACKAGE_NAME]
apktool-kotlin -i base.apk
```

now you need to sign all of the apk (if any multiple in the folder)

```
modder sign *.apk
```

now everything should be ready for testing
just delete the old apk from your phone
and install the original version

```
cd ..
modder install [APK_PACKAGE_NAME]
```
