#FLAC recorder
The original file is in WAV format, but this file will be converted into FLAC format, for google seems only allow this one.  
I didn't use absolute path to sign file name, audio file will be created on project path.  
Till now I only implement an voice recorder, which means it will record your voice and store it in a file.
So for now this module cannot handle streaming audio data respect to Google Full Duplex api, but I will try hard to finish this function.

###dependency
Notice I am using [`FLACEncoder`](http://sourceforge.net/projects/javaflacencoder/), but because this jar is not yet published in maven repository, so I installed it locally and named it with `net.sourceforge...`.   
If anyone want to test it on their own PC, please first install this lib by [maven install](http://rugal.ga/development/2014/08/24/install-local-jar-to-local-repository/) and I still love Maven.

    <dependency>
        <groupId>net.sourceforge</groupId>
        <artifactId>flacEncoder</artifactId>
        <version>1</version>
        <type>jar</type>
    </dependency>


Thanks to [Luke Kuza](https://github.com/lkuza2) and [Aaron Gokaslan](https://github.com/Skylion007), their [work](https://github.com/lkuza2/java-speech-api) actually helps me a lot.   