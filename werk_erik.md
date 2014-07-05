Controller Application
=====

Ik heb me bezig gehouden met het maken van de controller application om als user de robot te besturen. Ik heb hiervoor het framework gepakt van: http://staff.science.uva.nl/~edwin/Downloads/NCC/NC%2028-05-2014.zip
Van dit framework heb ik de layout aangepast en een bibliotheek toegevoegd, deze bibliotheek (http://zerokol.com/post/51ea7ff7e84c55ddae000005/1/en) bevat een custom View voor een joystick. Het ontwerp van de joystick heb ik aangepast, nieuw design is ontworpen door Abdel.
Extra features die ik heb toegevoegd is dat we alleen berichtje sturen naar de robot als de joystick veranderd ten op zichte van de vorige keer, het gaat hier om dezelfde 9 cases die Abdel noemt (N,NO,O,ZO,Z,ZW,W,NW,Rust).
Er is ook een grote SCAN-knop bijgekomen die de user kan gebruiken om te tikken, als de user niet de tikker is, is het niet mogelijk op de knop te klikken, dit kan alleen als de user de tikker is. Er wordt een extra bericht gestuurd naar de andere controllers zodat deze voor een kleine 3 seconden stil staan, dit hebben we gedaan zodat het makkelijker is om iemand te tikken. Dit geldt ook voor de tikker, die kan ook niet meer bewegen tot het scannen is gelukt of mislukt.

Problemen
=====

Ik ben relatief veel problemen tegen gekomen:
- Wilde eerst zelf een joystick maken, dat heb ik snel veranderd en ben op zoek gegaan naar sourcecode van iemand, wat vervolgend nog lastig was vinden van code die nog aan te passen was en niet alleen een .jar file was.
- Het toevoegen van de bibliotheek aan het project, in eclipse was dit gelukt alleen werkte het framework niet met eclipse. Uiteindelijk is het gelukt de bibliotheek toe te voegen in AndroidStudio.
- Door het bluetooth gebeurde af en toe dat berrichten niet aankwamen, daardoor gebeurde sommige acties niet of juist wel als het niet moest gebeuren. Dit hebben we opgelost door eenvoudigere code te schijven.
- Omdat een telefoon maar 1 master kan hebben, hebben we besloten een bluetooth cirkel te maken om de berichten toch bij iedereen te krijgen.
- Het liefst hadden we meerdere dingen geautomatiseerd willen krijgen, maar in dit timeframe is dat niet verwezenlijkt.
