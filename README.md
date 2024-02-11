<!-- [<img 
     src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">]() -->

<img src="https://github.com/Eluded-Smartphones/Eluded-Privacy-Manager/blob/main/app/src/main/res/drawable/logo.png?raw=true">

# The Aim:
This Project Aims To Combine:
- [Wasted](https://github.com/x13a/Wasted)
- [CalyX Firewall](https://gitlab.com/MarleyPlant/platform-packages-apps-securityfeatures)
- [Scrambled EXIF](https://gitlab.com/juanitobananas/scrambled-exif)
- [Duress](https://github.com/x13a/Duress)
- [Mission Control](https://f-droid.org/en/packages/net.kollnig.missioncontrol.fdroid/)
- [Network Ghost](https://github.com/souramoo/NetworkGhost)

With a more user-friendly interface 
which allows someone not so technical to configure privacy-friendly features:

## Features
* Network Routing
* Tracker Management
* Get Notified of potential threats
* Panic Wipe
     * fire when a device was not unlocked for X time
     * fire when a USB data connection is made while a device is locked
     * fire when a fake messenger app is launched
     * fire when a specific lock pattern is entered.
     * fire when a secretcode is found in notifications

Eventually the aim is to merge into [CalyxOS](https://calyxos.org/)

Be aware that the app does not work in _safe mode_.

# Broadcast

* action: `com.eluded.privacymanager.action.TRIGGER`
* receiver: `com.eluded.privacymanager/.TriggerReceiver`
* also you have to send a secret code from Wasted with the key: `code`

# Permissions

* DEVICE_ADMIN - lock and optionally wipe a device
* FOREGROUND_SERVICE - receive lock and USB state events
* RECEIVE_BOOT_COMPLETED - persist lock job and foreground service across reboots
* org.calyxos.datura.permission.CHANGE_SETTINGS - Update Settings For Firewall


# Localization

[![GNU GPLv3 Image](https://www.gnu.org/graphics/gplv3-127x51.png)](https://www.gnu.org/licenses/gpl-3.0.en.html)
