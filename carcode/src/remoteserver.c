#include <stdio.h>
#include <stdlib.h>

#include <netdb.h>
#include <netinet/in.h>

#include <string.h>
#include <strings.h>
#include <unistd.h>

#include "motordriver.h"
#include "remoteserver.h"

/* Remote command reference:
* 0 = left
* 1 = up
* 2 = right
* 3 = down
* 4 = stop left
* 5 = stop up
* 6 = stop right
* 7 = stop down
*/
        
struct motionstate carstate = {0,0,0,0};

/* Updates the car's motion according to the MotionState.
*/
int updateCarMotion(void) {
	char direction[16];
	if (carstate.forward && !carstate.back) {
		if ((!carstate.left && !carstate.right) || 
				(carstate.left && carstate.right)) {
			strcpy(direction, "forward");
			go_forward();
		} else if (carstate.left && !carstate.right) {
			strcpy(direction, "forward left");
			go_forward_left();
		} else if (carstate.right && !carstate.left) {
			strcpy(direction, "forward right");
			go_forward_right();
		} else {
			strcpy(direction, "stopped");
			stop();
		}
	} else if (carstate.back && !carstate.forward) {
		if ((!carstate.left && !carstate.right) || 
				(carstate.left && carstate.right)) {
			strcpy(direction, "back");
			go_back();
		} else if (carstate.left && !carstate.right) {
			strcpy(direction, "back left");
			go_back_left();
		} else if (carstate.right && !carstate.left) {
			strcpy(direction, "back right");
			go_back_right();
		} else {
			strcpy(direction, "stopped");
			stop();
		}
	} else if (carstate.left && !carstate.right) {
		strcpy(direction, "left");
		go_left();
	} else if (carstate.right && !carstate.left) {
		strcpy(direction, "right");
		go_right();
	} else {
		strcpy(direction, "stopped");
		stop();
	}
	printf("Motion: %s \n", direction);
return 0;
}

/* Updates the struct MotionState of the car.
*/
int updateCarState(char command) {
        switch (command) {
                case 0: /* left */
                	carstate.left = 1;
                        break;
                case 1: /* up */
                	carstate.forward = 1;
                        break;
                case 2: /* right */
                	carstate.right = 1;
                        break;
                case 3: /* down */
                	carstate.back = 1;
                        break;
                case 4: /* stop left */
                	carstate.left = 0;
                        break;
                case 5: /* stop up */
                	carstate.forward = 0;
                        break;
                case 6: /* stop right */
                	carstate.right = 0;
                        break;
                case 7: /* stop down */
                	carstate.back = 0;
                        break;
         }
	/*printf("State: %x,%x,%x,%x\n",carstate.left, carstate.forward,
		carstate.right, carstate.back);*/
return 0;
}

/* Creates a server socket and listens for a command from the remote.
*/
int main( int argc, char *argv[] ) {
	int sockfd, newsockfd, portno, clilen;
	char buffer;
        struct sockaddr_in serv_addr, cli_addr;
        int  n;
		
		/* Initialise GPIO */
		if (ControllerInit() < 0) return -1;

        /* First call to socket() function */
        sockfd = socket(AF_INET, SOCK_STREAM, 0);

        if (sockfd < 0) {
                perror("ERROR opening socket");
                exit(1);
        }

        /* Initialize socket structure */
        bzero((char *) &serv_addr, sizeof(serv_addr));
        portno = 11337;

        serv_addr.sin_family = AF_INET;
        serv_addr.sin_addr.s_addr = INADDR_ANY;
        serv_addr.sin_port = htons(portno);
 
        /* Now bind the host address using bind() call.*/
        if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
                perror("ERROR on binding");
                exit(1);
        }
      
        /* Now start listening for the clients, here process will
        * go in sleep mode and will wait for the incoming connection
        */

        listen(sockfd,5);
        clilen = sizeof(cli_addr);

        /* Accept actual connection from the client */
        newsockfd = accept(sockfd, (struct sockaddr *)&cli_addr, (void *) &clilen);

        if (newsockfd < 0) {
                perror("ERROR on accept");
                exit(1);
        }

        /* If connection is established then start communicating */
        bzero(&buffer, 1);
        while ((n = read(newsockfd, &buffer, 1)) > 0) {
                /*printf("Received: %x\n", buffer);*/
                updateCarState(buffer);
                updateCarMotion();
        }

        if (n < 0) {
                perror("ERROR reading from socket");
                exit(1);
        }
		
		ControllerShutdown();

        /* Write a response to the client */
        /*n = write(newsockfd,"I got your message",18);

        if (n < 0) {
                perror("ERROR writing to socket");
                exit(1);
        }*/
   
        return 0;
}
