/* Código de tres direcciones */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
double stack[100000];
double heap[100000];
double H = 0;
double P = 0;
double RET = 0;
double RA = 0;
double _ra = 0;
double _ri = 0;
char _txt[4096];
char _rc = 0;

int main(void) {
    P = 0;
    stack[(int)P + 2] = H;
    heap[(int)H] = 72;
    H = H + 1;
    heap[(int)H] = 111;
    H = H + 1;
    heap[(int)H] = 108;
    H = H + 1;
    heap[(int)H] = 97;
    H = H + 1;
    heap[(int)H] = 32;
    H = H + 1;
    heap[(int)H] = 77;
    H = H + 1;
    heap[(int)H] = 117;
    H = H + 1;
    heap[(int)H] = 110;
    H = H + 1;
    heap[(int)H] = 100;
    H = H + 1;
    heap[(int)H] = 111;
    H = H + 1;
    heap[(int)H] = 0;
    H = H + 1;
    _ri = stack[(int)P + 2];
_l1:;
    if (heap[(int)_ri] == 0) goto _l2;
    if (heap[(int)_ri] == -1) goto _l2;
    putchar((char)heap[(int)_ri]);
    _ri = _ri + 1;
    goto _l1;
_l2:;
    printf("\n");
    goto L_end;
L_end:
    return 0;
}
