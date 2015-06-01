#My little dream

I try to make a program that could use speech recognition to do some cool stuffs like programming by recognizing coder's word.   
like Jarvis in Iron man, cool huh!   
It is not very difficult, but it is rather difficult because of the dilemma faced by speech recognition techniques.  
If anyone has good idea about high accuracy recognizer, please with no hesitation to contribute.  

After did some researches, I find google speech api rather powerful, so I decided to use it as my speech recognizer.    
Now I have 3 modules, namely audio recorder, google speech and a keyboard robot.  But I added another module to integrate them into a so called robot. I hope it could match some of the programming standard.

My naive design file is in `/design` folder, for now it is a `visio` file.  

###audio recorder
Or Flac-recorder, is the voice recorder implemented in Java to save your voice and convert it into a FLAC format audio file, which is required by POSTing audio file to google speech API.  
Thanks to [Luke Kuza](https://github.com/lkuza2) and [Aaron Gokaslan](https://github.com/Skylion007), their [work](https://github.com/lkuza2/java-speech-api) actually helps me a lot.   
Notice I am using [`FLACEncoder`](http://sourceforge.net/projects/javaflacencoder/), thanks for their contribution. This jar ease my work a lot.  
But because this jar is not yet published in maven repository, so I installed it locally and named it with `net.sourceforge...`. If anyone want to test it on their own PC, please first install this lib by [maven install](http://rugal.ga/development/2014/08/24/install-local-jar-to-local-repository/).  


###google speech api
This module is responsible for wrapping your api key and posting http request with the flac file you provided to Google.  If anyone has problem with APIkey, you can refer to my [post](http://rugal.ga/developement/2015/05/16/tutorial-to-use-google-speech-api/).  
The `APIRequest` will return all String provided by google, which means there will have 2 json divided by a `\n`.  
I am using audio format with `sample rate = 16000` and `16 bits`. This specification results from a lot of experiments.  

###keyboard operator
My plan for this module is that, this daemon will listen to any instruction you give to it and trigger keyboard event to do work like human.  So later on it should listen to the transcript back from google speech API.  
Till now I implemented this module with basic function by using mouse click to instruct it. 
I will soon delete all those naive code and implement a real daemon robot.


###integration
My plan is, after finished these three modules, I will integrate them in this module for the sake of tidiness. I hate messy code. That's why I did a lot of changes and refactors to [J.A.R.V.I.S.](https://github.com/lkuza2/java-speech-api)