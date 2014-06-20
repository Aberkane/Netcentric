
Video streaming

Voor het project was er een livestream nodig van een Android naar Android toestel. Na wat adviezen te hebben gelezen op het  internet bleek het opensource project libstreaming het best geschikt voor ons project.  Een populaire Android applicatie die van deze bibliotheek gebruik maakt is WIFI Camera. Voor de initiële benchmarks hebben we de applicatie op een Android telefoon geïnstalleerd om te performance te meten. 
Na onderzoek bleek echter dat de framerate dusdanig laag was (<10 FPS) dat deze methode niet geschikt is voor live streaming.

Hierna vonden we het project Peepers dat geen gebruik maakt van de libstreaming bibliotheek maar wel streeft naar hetzelfde doel. Peepers maakt hierbij gebruik van de MediaEncoder API die al in Android zit verweven. Die video stream bestaat hierbij uit MJPEG (Motion-JPEG) wat in feiten bestaat uit het achtereenvolgens tonen van JPEG afbeeldingen. 

De prestaties hierbij waren boven verwachting. Een JPEG stream met een afmeting van 640x480 behaalden meer dan 20 FPS, wat ruim geschikt is voor een live video stream. Het nadeel van deze applicatie is dat het gebruikt video formaat alleen word ondersteunt door een Firefox browser.

