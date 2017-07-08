/*
   This code may be used to drive the Adafruit (or clones) Motor Shield.

   The code as written only supports DC motors.

   http://shieldlist.org/adafruit/motor

   The shield pinouts are

   D12 MOTORLATCH
   D11 PMW motor 1
   D10 Servo 1
   D9  Servo 2
   D8  MOTORDATA

   D7  MOTORENABLE
   D6  PWM motor 4
   D5  PWM motor 3
   D4  MOTORCLK
   D3  PWM motor 2

   The motor states (forward, backward, brake, release) are encoded using the
   MOTOR_ latch pins.  This saves four gpios.
*/

typedef unsigned char uint8_t;

/* assign gpios to drive the shield pins */

/*      Shield      Pi */

#define MOTORLATCH  25
#define MOTORCLK    17
#define MOTORENABLE 22
#define MOTORDATA   23

#define MOTOR_1_PWM 24
#define MOTOR_2_PWM 4
#define MOTOR_3_PWM 18
#define MOTOR_4_PWM 27

#define PWM_MAX 255 // Max PWM value.
#define PWM_HALF 127 // Half of the max PWM value.

/* Bit values for writing to the shift register. These correspond to bit numbers in latch_state. */

#define MOTOR1_A 2
#define MOTOR1_B 3
#define MOTOR2_A 1
#define MOTOR2_B 4
#define MOTOR4_A 0
#define MOTOR4_B 6
#define MOTOR3_A 5
#define MOTOR3_B 7

#define FORWARD  1
#define BACKWARD 2
#define BRAKE    3
#define RELEASE  4

void latch_tx(void);

int ControllerInit(void);

void ControllerShutdown(void);

void DCMotorInit(uint8_t num);

void DCMotorRun(uint8_t motornum, uint8_t cmd);

void TestRun(void);

void go_forward(void);

void go_left(void);

void go_right(void);

void go_forward_left(void);

void go_forward_right(void);

void go_back(void);

void go_back_left(void);

void go_back_right(void);

void stop(void);

