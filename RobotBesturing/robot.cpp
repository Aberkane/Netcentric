#include "mbed.h"
#include "Servo.h"

Servo motor1(p23);
Servo motor2(p24);
Servo motor3(p25);
 
int main() {    
    for(float p=0; p<0.6; p += 0.1) {
        motor1 = p;
        motor2 = motor1 - 0.1; 
        motor3 = motor2 - 0.1;
        wait(0.2);
    }
    
 
}