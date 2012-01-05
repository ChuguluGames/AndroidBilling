# AndroidBilling #

Demonstration how to use brunch and phonegap for a android application.<br>

# Dependencies #

* brunch
* PGT

# Installation #

Customize the android/.phonegap/config file.

# Usage #

Run <code>brunch build brunch</code><br>
To build, deploy and run on Android device, run <code>pgt -bdrt "device" -l "debug"</code>

# Limitations #

You need to put your assets files in the build directory.<br>
You need to put the same structure in the .android and .web folders.<br>
There is no automatism to copy the .web in the vendor folder, you'll need to do it manually.

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