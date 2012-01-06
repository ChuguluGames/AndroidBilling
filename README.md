# AndroidBilling #

Sample application with:<br>
* PhoneGap 1.3
* brunch
* PGT
* In-App Billing

# Dependencies #

* brunch
* PGT

# Installation #

Adapt the android/.phonegap/config file.<br>
Adapt the android/local.properties file.

# Usage #

Run <code>brunch build brunch</code><br>
To build, deploy and run on Android device, run <code>pgt -bdrt "device" -l "debug"</code>

# Limitations #

You need to put your assets files in the <strong>brunch/build</strong> directory.<br>
You need to put the <strong>same structure</strong> in the brunch/src/.android and brunch/src/.web folders.<br>
There is <strong>no automatism</strong> to copy the brunch/src/.web in the vendor folder, you'll need to do it manually.

# Application Structure #

<pre>
android
	.pgt
		builder # custom PGT builder
		config
	assets
		wwww
	[...]
brunch
	android # android webapp src
	build # compiled src | common to android & web
		assets
		[...]
	src
		.android # specific android files
		.web # specific web files
		app
		vendor
</pre>