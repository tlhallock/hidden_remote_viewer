#include "red_black.h"

#include <stdlib.h>

static red_black_node *red_black_node_new(void *value, red_black_node *left, red_black_node *right, red_black_node *parent)
{
  red_black_node *ret = (red_black_node *) malloc (sizeof(*ret));

  ret->value = value;
  ret->left = left;
  ret->right = right;
  ret->parent = parent;
  ret->red = true;

  return ret;
}

static int red_black_node_size(red_black_node *node)
{
  if (node == NULL)
  {
      return 0;
  }
  
  return 1 + red_black_node_size(node->left) + red_black_node_size(node->right);
}

int red_black_size(red_black *tree)
{
  return red_black_node_size(tree->root);
}

static void red_black_node_del(red_black_node *node)
{
  if (node == NULL)
  {
      return;
  }

  red_black_node_del(node->left);
  red_black_node_del(node->right);
  free(node);
}
















static red_black_node *red_black_node_grandparent(red_black_node *n)
{
 if ((n != NULL) && (n->parent != NULL))
  return n->parent->parent;
 else
  return NULL;
}
 
static red_black_node *red_black_node_uncle(red_black_node *n)
{
 red_black_node *g = red_black_node_grandparent(n);
 if (g == NULL)
  return NULL; // No grandparent means no uncle
 if (n->parent == g->left)
  return g->right;
 else
  return g->left;
}

static void red_black_node_rotate_left(red_black_node *node, red_black_node *g)
{
  if (g == NULL)
  {
     g = red_black_node_grandparent(node);
     if (g == NULL)
     {
        return;
     }
  }
  
  red_black_node *saved_p = g->left;
  red_black_node *saved_left_n = node->left;
    
  g->left = node;
  node->parent = g;
    
  node->left = saved_p;
  saved_p->parent = node;
    
  saved_p->right = saved_left_n;
  saved_left_n->parent = saved_p;

}

static void red_black_node_rotate_right(red_black_node *node, red_black_node *g)
{
  if (g == NULL)
  {
     g = red_black_node_grandparent(node);
     if (g == NULL)
     {
        return;
     }
  }
  
  red_black_node *saved_p = g->right;
  red_black_node *saved_right_n = node->right;
    
  g->right = node;
  node->parent = g;
    
  node->right = saved_p;
  saved_p->parent = node;
    
  saved_p->left = saved_right_n;
  saved_right_n->parent = saved_p;
}

static void red_black_node_balance(red_black_node *node)
{
   if (node == NULL || node->parent == NULL)
   {
      return;   
   }
   if (!node->parent->red)
   {
     return;  
   }
  
  red_black_node *u = red_black_node_uncle(node);
  
  if (u != NULL && u->red)
  {
    node->parent->red = false;
    u->red = false;
    red_black_node *g = red_black_node_grandparent(node);
    g->red = true;
    red_black_node_balance(g);
    return;
  }
  
  red_black_node *g = red_black_node_grandparent(node);
  if (g == NULL)
  {
    return;
  }
  
  if ((node == node->parent->right) && (node->parent == g->left))
  {
    red_black_node_rotate_left(node, g);
    node = node->left;
  }
  else if ((node == node->parent->left) && (node->parent == g->right))
  {
    red_black_node_rotate_right(node, g);
    node = node->right;
  }
  
  node->parent->red = false;
  g->red = true;
  if (node == node->parent->left)
  {
    red_black_node_rotate_right(g, NULL);
  }
  else
  {
    red_black_node_rotate_left(g, NULL);
  }
}





















/**
 * Do NOT call this with a null node.
 **/
static void red_black_node_insert(red_black_node *node, red_black_node *new_node, int (*compare)(void *,void *))
{
  red_black_node *prev = NULL;
  int compareVal;
  while (node != NULL)
  {
    prev = node;
    compareVal = compare(new_node->value, node->value);
    if (compareVal < 0)
    {
      node = node->left;
    }
    else
    {
      node = node->right;
    }
  }

  new_node->parent = prev;

  if (compareVal < 0)
  {
    prev->left = new_node;
  }
  else
  {
    prev->right = new_node;
  }

  red_black_node_balance(node);
}

/**
 * Do NOT call this with a null node
 */
static bool red_black_node_remove(red_black_node *node, void *value, int (*compare)(void *, void *))
{
  red_black_node *prev = NULL;
  int compareVal;

  red_black_node *root = node;

  while (node != NULL)
  {
    compareVal = compare(value, node->value);
    if (compareVal < 0)
    {
      prev = node;
      node = node->left;
    }
    else if (node->value != value)
    {
      prev = node;    
      node = node->right;
    }
    else
    {
      break;
    }
  }

  if (node == NULL)
  {
    return false;
  }

  if (prev == NULL)
  {
    puts("Removing element at the root node is currently not supported!");
    return false;
  }

  if (red_black_node_size(node->left) < red_black_node_size(node->right))
  {
    red_black_node *left_tree = node->left;
    
    if (node->right != NULL)
    {
      node->right->parent = prev;
    }
    if (prev->left == node)
    {
      prev->left = node->right;
    }
    else
    {
      prev->right = node->right;
    }
    if (left_tree != NULL)
    {
      red_black_node_insert(prev, left_tree, compare);
    }
  }
  else
  {
    red_black_node *right_tree = node->right;

    if (node->left != NULL)
    {
      node->left->parent = prev;
    }
    if (prev->left == node)
    {
      prev->left = node->left;
    }
    else
    {
      prev->right = node->left;
    }
    if (right_tree != NULL)
    {
      red_black_node_insert(prev, right_tree, compare);
    }
  }

  node->left = NULL;
  node->right = NULL;
  red_black_node_del(node);

  return true;
}

red_black *red_black_new(int (*compare)(void *, void *))
{
  red_black *ret = (red_black *) malloc (sizeof(*ret));

  ret->root = NULL;
  ret->compare = compare;

  return ret;
}

void red_black_del(red_black *tree)
{
  red_black_node_del(tree->root);
  free(tree);
}

void red_black_insert(red_black *tree, void *value)
{
  red_black_node *node = red_black_node_new(value, NULL, NULL, NULL);
  if (tree->root == NULL)
  {
    tree->root = node;
    tree->root->red = false;
  }
  else
  {
    red_black_node_insert(tree->root, node, tree->compare);
  }
}

bool red_black_remove(red_black *tree, void *value)
{
  if (tree->root == NULL)
  {
    return;
  }

  red_black_node_remove(tree->root, value, tree->compare);
}


static void print_indents(int num, FILE *out)
{
  int i;
  for (i=0;i<num;i++)
  {
    fputc('\t', out);
  }
}

static void red_black_node_print(red_black_node *node, int depth, FILE *out)
{
  if (node == NULL)
  {
    print_indents(depth, out);
    fprintf(out, "NULL\n");
    return;
  }

  red_black_node_print(node->left, depth + 1, out);
  print_indents(depth, out);
  fprintf(out, "%s:%d:%p\n", node->red ? "red" : "black", depth, node->value);
  red_black_node_print(node->right, depth + 1, out);
}

void red_black_print(red_black *tree, FILE *out)
{
  fprintf(out, "compare = %p\n", tree->compare);
  red_black_node_print(tree->root, 0, out);
}


int red_black_node_count(red_black_node *node, void *value, int (*compare)(void *, void *))
{
  if (node == NULL)
  {
     return 0;
  }
  
  int compareVal = compare(value, node->value);
  
  int count = 0;
  if (compareVal <= 0)
  {
    count += red_black_node_count(node->left, value, compare);
  }
  if (compareVal >= 0)
  {
    count += red_black_node_count(node->right, value, compare);
  }
  if (node->value == value)
  {
    count++;
  }
  return count;
}

int red_black_count(red_black *tree, void *value)
{
  return red_black_node_count(tree->root, value, tree->compare); 
}

static red_black_node_verify(red_black_node *node, void **current, int (*compare)(void *, void *))
{
	if (node == NULL)
	{
		return true;
	}
	
	if (node->left != NULL)
	{
		if (node->left->parent != node)
		{
			puts("broken parent");
			return false;
		}
	}
	if (node->right != NULL)
	{
		if (node->right->parent != node)
		{
			puts("broken parent");
			return false;
		}
	}
		
	
	if (!red_black_node_verify(node->left, current, compare))
	{
		return false;
	}
	if (compare(node->value, *current) < 0)
	{
		puts("Not sorted!\n");
		return false;
	}
	current = &node->value;
	if (!red_black_node_verify(node->right, current, compare))
	{
		return false;
	}
	return true;
}

bool red_black_verify(red_black *tree)
{
	return red_black_node_verify(tree->root, &tree->root->value, tree->compare);
}
