Besturing
=====

Ik heb me sinds het begin beziggehouden met de besturing van de robot. 
http://www.eit.lth.se/fileadmin/eit/courses/edi021/Sammanfattning/2005/lp-2/grupp4/avrcode.txt
Uit deze code heb ik het idee gehaald om een switch te gebruiken. 
In overleg met Erik heb ik aan de robot 9 cases toegekend. Hij kan naar voren en naar achter, rechtsvoor, linksvoor, rechtsachter en linksachter bewegen. Daarnaast kan hij ook beide kanten op roteren en stilstaan. Bij het coderen van de besturing heb ik gebruik gemaakt van de Servo bibliotheek.  Voordat de motoren van de robot worden aangestuurd, worden ze eerst gekalibreerd zodat ze daadwerkelijk stil staan. We hebben meegemaakt dat een motor uit zichzelf heel licht roteerde. Dat wilden we niet hebben.

We hebben ook ervoor gekozen om bij bepaalde richtingen de rotatiesnelheid aan te passen. Bijvoorbeeld als de robot slechts wil roteren, dan draaien alle drie de wielen op dezelfde snelheid de zelfde kant op. Echter, als de robot bijvoorbeeld naar rechtsvoor wil, dan draaien de wielen die er voor zorgen dat de robot vooruit gaat op volle snelheid. Het wiel dat voor de rotatie zorgt draait ongeveer op de helft van de volledige snelheid. Dit sorteerde in het gewenste effect van schuin naar voren of schuin naar achter rijden. 

Al het bovenstaande is geïmplementeerd in een simpele snelle functie genaamd control_robot(). De functie neemt dus een int als argument, de int geeft aan welke case uitgevoerd moet worden. De int wordt aangegeven vanuit Erik’s code. Hij heeft een framework gemaakt, waarin een joystick zit. Die joystick heb ik gecreëerd met Photoshop. Hiernaast heb ik ook geholpen bij mijn groepsgenoten en zij ook bij mij.
