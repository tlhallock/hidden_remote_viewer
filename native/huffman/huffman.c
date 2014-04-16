#include <stdio.h>

struct huffman_node_
{
  int val;
  int priority;

  struct huffman_node_ *left;
  struct huffman_node_ *right;
};

typedef struct huffman_node_ huffman_node;

huffman_node *huffman_node_new(int value, huffman_node *left, thuffman_node *right)
{
  huffman_node *ret = (huffman_node *) malloc( sizeof( *ret));
  ret->priority = 0;
  ret->val = value;
  ret->left = left;
  ret->right = right;
  return ret;
}

void huffman_node_del(huffman_node *todel)
{
  if (todel->left != NULL)
  {
    huffman_node_del(todel->left);
  }
  if (todel->right != NULL)
  {
    huffman_node_del(todel->left);
  }
  free(todel);
}



