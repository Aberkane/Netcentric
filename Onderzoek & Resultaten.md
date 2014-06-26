
Video streaming
=========

Voor het project was er een livestream nodig van een Android naar Android toestel. Na wat adviezen te hebben gelezen op het  internet bleek het opensource project libstreaming het best geschikt voor ons project.  Een populaire Android applicatie die van deze bibliotheek gebruik maakt is WIFI Camera. Voor de initiële benchmarks hebben we de applicatie op een Android telefoon geïnstalleerd om te performance te meten. 
Na onderzoek bleek echter dat de framerate dusdanig laag was (<10 FPS) dat deze methode niet geschikt is voor live streaming.

Hierna vonden we het project Peepers dat geen gebruik maakt van de libstreaming bibliotheek maar wel streeft naar hetzelfde doel. Peepers maakt hierbij gebruik van de MediaEncoder API die al in Android zit verweven. Die video stream bestaat hierbij uit MJPEG (Motion-JPEG) wat in feiten bestaat uit het achtereenvolgens tonen van JPEG afbeeldingen. 

De prestaties hierbij waren boven verwachting. Een JPEG stream met een afmeting van 640x480 behaalden meer dan 20 FPS, wat ruim geschikt is voor een live video stream. Het nadeel van deze applicatie is dat het gebruikt video formaat alleen word ondersteunt door een Firefox browser.

Robot identificatie
=========

Voor het te implementeren spel zochten wij naar een manier om robots te kunnen identificeren. 
Slechts beschikkende over sensoren is de keus gevallen op een QR code die op elke robot word geplaatst en door andere robots kan worden gescand. Zodoende kunnen robots elkaar herken en “tikken”. 

De bekendste bibliotheek voor het scannen van Qr codes is Zxing. De Zxing libary is ontzettend breed en beschikbaar voor verschillende platformen. Het bijzondere aan Zxing is dat het als een normale applicatie kan worden geïnstalleerd waarna via een Intent de applicatie kan worden aangeroepen om een code te scannen. Het nadeel hiervan is echter dat de Parrent Activty word gepauzeerd en de robot dus niet kan bewegen. Dit kan gedeeltelijk worden opgelost door de inactivity time in InactivityTimer.java aan te passen. Door deze tijd te verkorten heeft de gebruiker nog steeds de optie om een barcode te scannen. Is er een barcode gevonden dan retourneert Zxing de controller gelijk aan de Parent applicatie. Als er binnen 3 seconden geen barcode is gescand dan worden Zxing geforceerd gesloten zodat de Parrent applicatie alsnog te controller terugkrijgt. 

In eerste instantie werd gekozen voor barcodes ter identificatie. Na wat experimenteren bleken barcodes (Van het type: Code 128-B) toch iets langzamer te worden herkend en is besloten om QR codes te prefereren.
