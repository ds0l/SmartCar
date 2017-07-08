#include <stdio.h>
#include <pigpio.h>
#include <unistd.h>
#include "motordriver.h"

#define BIT(bit) (1 << (bit))

static uint8_t latch_state; // The state of the shift register on the physical board.

/*
Write to shift register.
*/
void latch_tx(void)
{
   unsigned char i;

   gpioWrite(MOTORLATCH, PI_LOW);

   gpioWrite(MOTORDATA, PI_LOW);

   for (i=0; i<8; i++)
   {
      gpioDelay(10);  // 10 micros delay

      gpioWrite(MOTORCLK, PI_LOW);

      if (latch_state & BIT(7-i)) 
	  {
		  gpioWrite(MOTORDATA, PI_HIGH);
	  }
      else
	  {
		  gpioWrite(MOTORDATA, PI_LOW);
	  }

      gpioDelay(10);  // 10 micros delay

      gpioWrite(MOTORCLK, PI_HIGH);
   }

   gpioWrite(MOTORLATCH, PI_HIGH);
   return;
}

/*
* Initialize GPIOs and shift register.
*/
int ControllerInit(void)
{
	if (gpioInitialise() < 0) {
		printf("gpioInitialise failed.\n");
		return -1;
	}
	
	gpioSetMode(MOTORLATCH,  PI_OUTPUT);
	gpioSetMode(MOTORENABLE, PI_OUTPUT);
	gpioSetMode(MOTORDATA,   PI_OUTPUT);
	gpioSetMode(MOTORCLK,    PI_OUTPUT);
   
	gpioSetMode(MOTOR_1_PWM, PI_OUTPUT);
	gpioSetMode(MOTOR_2_PWM, PI_OUTPUT);
	gpioSetMode(MOTOR_3_PWM, PI_OUTPUT);
	gpioSetMode(MOTOR_4_PWM, PI_OUTPUT);

	gpioPWM(MOTOR_1_PWM, 0);
	gpioPWM(MOTOR_2_PWM, 0);
	gpioPWM(MOTOR_3_PWM, 0);
	gpioPWM(MOTOR_4_PWM, 0);
	
	latch_state = 0;
	
	latch_tx();
	
	gpioWrite(MOTORENABLE, PI_LOW);
	
	gpioPWM(MOTOR_1_PWM, PWM_MAX);
	gpioPWM(MOTOR_2_PWM, PWM_MAX);
	gpioPWM(MOTOR_3_PWM, PWM_MAX);
	gpioPWM(MOTOR_4_PWM, PWM_MAX);
	
return 0;
}

/*
* Sets everything to LOW and calls gpioTerminate().
*/
void ControllerShutdown(void)
{
    gpioPWM(MOTOR_1_PWM, 0);
    gpioPWM(MOTOR_2_PWM, 0);
    gpioPWM(MOTOR_4_PWM, 0);
    gpioPWM(MOTOR_3_PWM, 0);

    DCMotorRun(1, RELEASE);
    DCMotorRun(2, RELEASE);
    DCMotorRun(3, RELEASE);
    DCMotorRun(4, RELEASE);

   gpioTerminate();
   return;
}

/*
* Reset a motor?
*/
void DCMotorInit(uint8_t num)
{
   switch (num)
   {
      case 1: latch_state &= ~BIT(MOTOR1_A) & ~BIT(MOTOR1_B);
      case 2: latch_state &= ~BIT(MOTOR2_A) & ~BIT(MOTOR2_B); 
      case 3: latch_state &= ~BIT(MOTOR3_A) & ~BIT(MOTOR3_B); 
      case 4: latch_state &= ~BIT(MOTOR4_A) & ~BIT(MOTOR4_B); 
      default: return;
   }

   latch_tx();

   //printf("Latch=%08X\n", latch_state);
   return;
}

/*
* Change latch_state and write it out to the shift register.
*/
void DCMotorRun(uint8_t motornum, uint8_t cmd)
{
   uint8_t a, b;

   switch (motornum)
   {
      case 1: 
	  	a = MOTOR1_A; 
		b = MOTOR1_B;
		break;
      case 2: 
	  	a = MOTOR2_A; 
		b = MOTOR2_B;
		break;
      case 3: 
	  	a = MOTOR3_A; 
		b = MOTOR3_B;
		break;
      case 4: 
	  	a = MOTOR4_A; 
		b = MOTOR4_B;
		break;
      default: return;
   }
 
   switch (cmd)
   {
      case FORWARD:  
	  	latch_state |=  BIT(a);
		latch_state &= ~BIT(b);
		break;
      case BACKWARD: 
	  	latch_state &= ~BIT(a);
	  	latch_state |=  BIT(b);
		break;
      case RELEASE:  
	  	latch_state &= ~BIT(a);
		latch_state &= ~BIT(b);
		break;
      default: return;
   }

   latch_tx();

   //printf("Latch=%08X\n", latch_state);
   return;
}


void go_forward(void) {
	DCMotorRun(1, FORWARD);
	DCMotorRun(2, FORWARD);
	DCMotorRun(3, FORWARD);
	DCMotorRun(4, FORWARD);
return;
}

void go_left(void) {
	DCMotorRun(1, FORWARD);
	DCMotorRun(2, BACKWARD);
	DCMotorRun(3, BACKWARD);
	DCMotorRun(4, FORWARD);
return;
}

void go_right(void) {
	DCMotorRun(1, BACKWARD);
	DCMotorRun(2, FORWARD);
	DCMotorRun(3, FORWARD);
	DCMotorRun(4, BACKWARD);
return;
}

void go_forward_left(void) {
	DCMotorRun(1, FORWARD);
	DCMotorRun(2, RELEASE);
	DCMotorRun(3, RELEASE);
	DCMotorRun(4, FORWARD);
return;
}

void go_forward_right(void) {
	DCMotorRun(1, RELEASE);
	DCMotorRun(2, FORWARD);
	DCMotorRun(3, FORWARD);
	DCMotorRun(4, RELEASE);
return;
}

void go_back(void) {
	DCMotorRun(1, BACKWARD);
	DCMotorRun(2, BACKWARD);
	DCMotorRun(3, BACKWARD);
	DCMotorRun(4, BACKWARD);
return;
}

void go_back_left(void) {
	DCMotorRun(1, BACKWARD);
	DCMotorRun(2, RELEASE);
	DCMotorRun(3, RELEASE);
	DCMotorRun(4, BACKWARD);
return;
}

void go_back_right(void) {
	DCMotorRun(1, RELEASE);
	DCMotorRun(2, BACKWARD);
	DCMotorRun(3, BACKWARD);
	DCMotorRun(4, RELEASE);
return;
}

void stop(void) {
	DCMotorRun(1, RELEASE);
	DCMotorRun(2, RELEASE);
	DCMotorRun(3, RELEASE);
	DCMotorRun(4, RELEASE);
return;
}
/*
* A test.
*/
void TestRun(void)
{
    DCMotorRun(1, FORWARD);
    sleep(2);
    DCMotorRun(1, RELEASE);

    DCMotorRun(2, FORWARD);
    sleep(2);
    DCMotorRun(2, RELEASE);

    DCMotorRun(3, FORWARD);
    sleep(2);
    DCMotorRun(3, RELEASE);

    DCMotorRun(4, FORWARD);
    sleep(2);
    DCMotorRun(4, RELEASE);

    sleep(1);
return;
}

/*
* Main

int main (int argc, char *argv[])
{
	if (gpioInitialise() < 0) return 1;
   
	ControllerInit();

	TestRun();

	ControllerShutdown();

   return 0;
}
*/

