#Google Speech API
Google allow WAV and FLAC format with different sample rate, don't know what else google allowed.  
After several times of experiments, I found FLAC with sample `rate=16000` in `16 bits` is good and fast enough. This is what I am using in google speech API now.  
Only recording audio is allowed for now, but I plan to use Google Full Duplex, which means I need to implement streaming recording. I got no idea about this technique.  



##Usage
Please refer to the [test case](https://github.com/Rugal/oral-programming/blob/master/google-speech/src/test/java/ml/rugal/googlespeech/request/RequesterTest.java#L36) I am doing.  
Notice google will throw `2` json back, the first with empty body is to confirm succeed of request. So you need to handle this by yourself.  
I don't know when will google change the format, but I wish it stay the same.  


##language support
If you want to set a language, please use:  
    
    requset.setLang(Lang.New_Zealand.getLang());

For more language support please refer to [speech](https://www.google.com/intl/en/chrome/demos/speech.html), line 196.  I copied them to the enumerator `Lang` for convenience.  
I am not responsible for the language support.  

####Language supported by Google

        [['Afrikaans',       ['af-ZA']],
         ['Bahasa Indonesia',['id-ID']],
         ['Bahasa Melayu',   ['ms-MY']],
         ['Català',          ['ca-ES']],
         ['Čeština',         ['cs-CZ']],
         ['Dansk',           ['da-DK']],
         ['Deutsch',         ['de-DE']],
         ['English',         ['en-AU', 'Australia'],
                             ['en-CA', 'Canada'],
                             ['en-IN', 'India'],
                             ['en-NZ', 'New Zealand'],
                             ['en-ZA', 'South Africa'],
                             ['en-GB', 'United Kingdom'],
                             ['en-US', 'United States']],
         ['Español',         ['es-AR', 'Argentina'],
                             ['es-BO', 'Bolivia'],
                             ['es-CL', 'Chile'],
                             ['es-CO', 'Colombia'],
                             ['es-CR', 'Costa Rica'],
                             ['es-EC', 'Ecuador'],
                             ['es-SV', 'El Salvador'],
                             ['es-ES', 'España'],
                             ['es-US', 'Estados Unidos'],
                             ['es-GT', 'Guatemala'],
                             ['es-HN', 'Honduras'],
                             ['es-MX', 'México'],
                             ['es-NI', 'Nicaragua'],
                             ['es-PA', 'Panamá'],
                             ['es-PY', 'Paraguay'],
                             ['es-PE', 'Perú'],
                             ['es-PR', 'Puerto Rico'],
                             ['es-DO', 'República Dominicana'],
                             ['es-UY', 'Uruguay'],
                             ['es-VE', 'Venezuela']],
         ['Euskara',         ['eu-ES']],
         ['Filipino',        ['fil-PH']],
         ['Français',        ['fr-FR']],
         ['Galego',          ['gl-ES']],
         ['Hrvatski',        ['hr_HR']],
         ['IsiZulu',         ['zu-ZA']],
         ['Íslenska',        ['is-IS']],
         ['Italiano',        ['it-IT', 'Italia'],
                             ['it-CH', 'Svizzera']],
         ['Lietuvių',        ['lt-LT']],
         ['Magyar',          ['hu-HU']],
         ['Nederlands',      ['nl-NL']],
         ['Norsk bokmål',    ['nb-NO']],
         ['Polski',          ['pl-PL']],
         ['Português',       ['pt-BR', 'Brasil'],
                             ['pt-PT', 'Portugal']],
         ['Română',          ['ro-RO']],
         ['Slovenščina',     ['sl-SI']],
         ['Slovenčina',      ['sk-SK']],
         ['Suomi',           ['fi-FI']],
         ['Svenska',         ['sv-SE']],
         ['Tiếng Việt',      ['vi-VN']],
         ['Türkçe',          ['tr-TR']],
         ['Ελληνικά',        ['el-GR']],
         ['български',       ['bg-BG']],
         ['Pусский',         ['ru-RU']],
         ['Српски',          ['sr-RS']],
         ['Українська',      ['uk-UA']],
         ['한국어',            ['ko-KR']],
         ['中文',             ['cmn-Hans-CN', '普通话 (中国大陆)'],
                             ['cmn-Hans-HK', '普通话 (香港)'],
                             ['cmn-Hant-TW', '中文 (台灣)'],
                             ['yue-Hant-HK', '粵語 (香港)']],
         ['日本語',           ['ja-JP']],
         ['हिन्दी',            ['hi-IN']],
         ['ภาษาไทย',         ['th-TH']]];