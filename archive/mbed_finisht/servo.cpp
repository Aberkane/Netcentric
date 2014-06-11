#include "mbed.h"

Serial pc(USBTX, USBRX);         
         
DigitalOut myled1(LED1),
           myled2(LED2),
           myled3(LED3),
           myled4(LED4),
           rotatieR(p15),
           rotatieL(p18);

AnalogIn mypin(p16);

float func(float x) {
   return  1.0940883381103002e-003 * pow(x,0)
        +  3.3836477185904892e-002 * pow(x,1)
        + -8.6168336840589527e-002 * pow(x,2)
        +  7.2898864709400185e-002 * pow(x,3)
        +  6.5634869157701475e-003 * pow(x,4)
        + -4.3041284300853841e-002 * pow(x,5)
        +  2.7865035485863790e-002 * pow(x,6)
        + -8.4485921561660623e-003 * pow(x,7)
        +  1.2295215776175377e-003 * pow(x,8)
        + -3.6304431194088678e-005 * pow(x,9)
       + -1.1407813908880530e-005 * pow(x,10)
       +  6.9455207057115884e-007 * pow(x,11)
       +  1.5831820248467315e-007 * pow(x,12)
       + -2.4464645786585510e-008 * pow(x,13)
       +  1.2748257280927221e-009 * pow(x,14)
       +  6.4620006332451161e-011 * pow(x,15)
       + -2.8653037259852716e-011 * pow(x,16)
       +  3.3937637701018807e-012 * pow(x,17)
       + -1.4775025027190523e-013 * pow(x,18)
       + -8.2774635566028758e-016 * pow(x,19)
       +  1.6178431560148211e-016 * pow(x,20);
}

void rotate(float from, float to)
{
    myled1 = 1;
    myled2 = 1;
    myled3 = 1;
    myled4 = 1;
    
    float marge = to / 100000;
    if(from < (to - marge))
    {
        if(to > 1.0 || (to - marge) > 1.0)
        {
            marge = 0;
            to = 1.0;
        }
        while(from < (to - marge))
        {
            from = mypin;
            rotatieR = 1;
            rotatieL = 0;
        }
        rotatieR = 0;
        rotatieL = 0;
    }
    else if(from > (to + marge))
    {
        if(to < 0.0 || (to + marge) < 0.0)
        {
            marge = 0.0;
            to = 0.0;
        }
        while(from > (to + marge))
        {
            from = mypin;
            rotatieR = 0;
            rotatieL = 1;
        }
        rotatieR = 0;
        rotatieL = 0;
    }
    
    myled1 = 0;
    myled2 = 0;
    myled3 = 0;
    myled4 = 0;
}



void callServo(float input){
    float servoValue;
    rotatieL = 0;
    rotatieR = 0;
    servoValue = mypin;
    rotate(servoValue, func(input));
    
}


/*int main() {
    float servoValue, inputFloat;
    char buffer[3];
    rotatieL = 0;
    rotatieR = 0;
    while(1) {
        servoValue = mypin;

        pc.printf("Geef waarde met 1 decimaal, 0.0 t/m 9.9(bv 0.0 or 1.3): ");
        
        pc.gets(buffer, 4);
        pc.printf("\n");
        inputFloat = atof(buffer);
        
        if(inputFloat >= 0.0 && inputFloat <= 9.9)
        {
            pc.printf("Draaien naar:(%g)\n", inputFloat);
            rotate(servoValue, func(inputFloat));
        }
        else
        {
            pc.printf("Waarde (%s) niet herkend, geef een waarde met 1 decimaal van 0.0 t/m 9.9. bv: 1.0 of 1.6\n", buffer);
        }
        
        
            
    }
}*/
