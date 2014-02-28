/*  beep - just what it sounds like, makes the console beep - but with
 * precision control.  See the man page for details.
 *
 * Try beep -h for command line args
 *
 * This code is copyright (C) Johnathan Nightingale, 2000.
 *
 * This code may distributed only under the terms of the GNU Public License 
 * which can be found at http://www.gnu.org/copyleft or in the file COPYING 
 * supplied with this code.
 *
 * This code is not distributed with warranties of any kind, including implied
 * warranties of merchantability or fitness for a particular use or ability to 
 * breed pandas in captivity, it just can't be done.
 *
 * Bug me, I like it:  http://johnath.com/  or johnath@johnath.com
 */

#include <fcntl.h>
#include <getopt.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <linux/kd.h>

#define CLOCK_TICK_RATE 1193180

int  usleep(unsigned long time);

void beep_short() 
{
    int console_fd = -1;
    // try to snag the console
    if((console_fd = open("/dev/console", O_WRONLY)) == -1) {
        fprintf(stderr, "Could not open /dev/console for writing.\n");
        perror("open");
        exit(1);
    }
  
    if(ioctl(console_fd, KIOCSOUND, CLOCK_TICK_RATE / 1000) < 0) {
        perror("ioctl");
    }
    // wait
    usleep(100000);
    // stop
    ioctl(console_fd, KIOCSOUND, 0);                    /* stop beep  */

    close(console_fd);
}

void beep_long() 
{
    int console_fd = -1;
    // try to snag the console
    if((console_fd = open("/dev/console", O_WRONLY)) == -1) {
        fprintf(stderr, "Could not open /dev/console for writing.\n");
        perror("open");
        exit(1);
    }
  
    if(ioctl(console_fd, KIOCSOUND, CLOCK_TICK_RATE / 1000) < 0) {
        perror("ioctl");
    }
    // wait
    usleep(300000);
    // stop
    ioctl(console_fd, KIOCSOUND, 0);                    /* stop beep  */

    close(console_fd);
}

void sound(const char* c) 
{
    while (*c != 0) {
        if (*c == '-') {
            beep_long();
        } else {
            beep_short();
        }
        usleep(300000);
        ++c;
    }
}
