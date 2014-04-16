#include <stdbool.h>
#include <stdio.h>

#ifndef READ_BLACK_HEADER_
#define READ_BLACK_HEADER_

struct red_black_node_
{
  bool red;

  void *value;

  struct red_black_node_ *parent;

  struct red_black_node_ *left;
  struct red_black_node_ *right;
};

typedef struct red_black_node_ red_black_node;

struct red_black_
{
  int (*compare)(void*, void*);
  red_black_node *root;
};

typedef struct red_black_ red_black;

red_black *red_black_new(int (*compare)(void *, void *));
void red_black_del(red_black *tree);

void red_black_insert(red_black *tree, void *value);
bool red_black_remove(red_black *tree, void *value);

void red_black_print(red_black *tree, FILE *out);

bool red_black_verify(red_black *tree);

int red_black_count(red_black *tree, void *value);
int red_black_size(red_black *tree);

#endif
