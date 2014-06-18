#include "mbed.h"
#include "Servo.h"

Servo motor1(p23);
Servo motor2(p24);
Servo rotor(p25);

Serial pc(USBTX, USBRX);

void control_robot(int command) {
    while(1) {                   
        switch(command) {
            /* Roteren en naar voren bewegen */
            // NW
            case 2: 
                motor1 = -1.0;
                motor2 = 1.0;
                rotor = -1.0;
                break;
            // N
            case 3:  
                motor1 = -1.0;
                motor2 = 1.0;
                break;
            // NO
            case 4: 
                motor1 = -1.0;
                motor2 = 1.0;
                rotor = 1.0; 
                break;
            // ZW
            case 8: 
                motor1 = 1.0;
                motor2 = -1.0;
                rotor = -1.0; 
                break; 
            // Z
            case 7: 
                motor1 = 1.0;
                motor2 = -1.0;
                break;
            // ZO
            case 6: 
                motor1 = 1.0;
                motor2 = -1.0;
                rotor = 1.0; 
                break; 
            
            /* Alleen roteren */
            // W
            case 1: 
                rotor = -1.0;
                motor1 = -1.0;
                motor2 = -1.0;
                break; 
            // O
            case 5: 
                rotor = 1.0;
                motor1 = 1.0;
                motor2 = 1.0; 
                break; 
        }
    }
}

int main() {
    printf("Servo Calibration Controls:\n");
    int input = 1;
    control_robot(input);
}