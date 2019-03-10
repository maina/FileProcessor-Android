# FileProcessor-Android
Picks csv file from the assets folder and sends to the server
* A CSV file has been saved in the assets folder. You can modify the file but keep the name as users.csv
* Remember to the run the web application first before running the client app
* If running from the emulator, go to build variants and choose - **localAuto**. This option auto detects your server IP if you 
are running the web server from within your machine 
* Else choose **emulatorDebug** to use the hard coded localhost server IP location as http://10.0.2.2:8080
* If above options don't work for you because may be the server is running from some remote machine you can enter the IP of your 
server in the section **hardCoded** of the app build.gradle. Set your build variant as **hardCoded** before running the app to use the IP you just set

