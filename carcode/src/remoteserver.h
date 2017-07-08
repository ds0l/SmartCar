
struct motionstate {
        unsigned int left:1;
        unsigned int forward:1;
        unsigned int right:1;
        unsigned int back:1;
        }; 

int updateCarMotion(void);

int updateCarState(char command);

