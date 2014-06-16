#include "mbed.h"
#include "Motor.h"
 
/*PwmOut motor1(p25);
PwmOut motor2(p24);
PwmOut motor3(p23);
 
Serial pc(USBTX, USBRX);



int main() {
    char buf[1];
    float inputF;
    while(1) {
        pc.printf("Noord = 1, Oost = 2, Zuid = 3, West = 4: ");        
        pc.gets(buf, 2);
        inputF = atof(buf);


        
        if (inputF == 1) {
            pc.printf("Input is %g", inputF);
            for(float p = 0.0f; p < 1.0f; p += 0.1f) {
                motor1 = p;
                motor2 = p;
                motor3 = p;
 
                wait(0.1);
            }       
        }

    }
}*/