#include <stdio.h>

#ifndef HUFFMAN_HEADER_
#define HUFFMAN_HEADER_

void huffman_encode(char *bytes, FILE *output);
void huffman_decode(FILE *input, FILE *output);


#endif
