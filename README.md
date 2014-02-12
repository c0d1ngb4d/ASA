				ASA

Release v1.0: 
	-Needs rooted device and Cydia Substrate installed
	-Stable in Android 4.0.4

Android, an operating system based on the Linux kernel, has millions of
users around the world. Its security scheme to protect a user from abusive
applications is all or nothing. When a user wants to install an application,
she must accept every permission it’s requiering as a whole, without the
chance to change them once they were granted.
For lack of a better implementation, we developed an application that al-
lows a more granular access control to the resources we consider of main
importance from the user’s privacy and security point of view.
When an application asks to access a resource, our approach allows the user
not just to retrieve false or anonymous information, but to select a subset
of the real data, so she can keep on using the applications in a controlled
manner. This way, a user can lie consciously in order to protect herself from
a possible abuse of trust that some applications may attemp. The Android
security architecture doesn’t address this kind of trust problems. In other
related works where the user can only retrieve fake data, access control af-
fects the usability when controlling a key resource for the application. For
example, the user clearly did not intend using WhatsApp with an empty
contacts list.
We’ve accomplished control access to contacts, data that allows a device and
its owner to be unambiguously identified such as its id, subscriber id, sim
card and phone number, device’s network information such as its IP, MAC,
the SSID and BSSID of the access point it is connected to, the configured
and scanned networks and, finally, we control every way an application can
know the device’s location, not only using the gps but also the cell towers
and configured wireless networks.

Tested on a Xperia S Sony device and a Samsung Galaxy S3.

Contributors:
	Ayelén Chavez 	- ashy.on.line@gmail.com	
	Joaquín Rinaudo - jmrinaudo@gmail.com

