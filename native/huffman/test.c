#include "red_black.h"

#include <unistd.h>
#include <stdlib.h>

int compare(void *val1, void *val2)
{
  return ((int)val2) - ((int)val1);
}

#define NUMS 10

int main(int argc, char **argv)
{
	
	srand(50000);
	
	int rands[NUMS];
	int i;
	for (i=0;i<NUMS;i++)
	{
		rands[i] = rand() % 16;
	}
	
	red_black *tree = red_black_new(&compare);
  	red_black_insert(tree, (void *) 0xffff);
  
	for (;;)
	{
		int next = rand() % 10;
		if (red_black_size(tree) > 15 || rand() % 1)
		{
			printf("Removing all %p\n", (void *) next);
			int num = red_black_count(tree, (void *) next);
			for (i=0;i<num;i++)
			{
				printf("%d\n", i);
				if (!red_black_remove(tree, (void *) next))
				{
					puts("Could not delete");
					exit(1);
				}
			}
			if (red_black_count(tree, (void *) next) != 0)
			{
				puts("still more");
				exit(1);
			}
		}
		else
		{
			printf("Adding %p\n", (void *) next);
			int count = red_black_count(tree, (void *) next);
			red_black_insert(tree, (void *)next);
			if (red_black_count(tree, (void *) next) != count + 1)
			{
				puts("Didn't add!");
				exit(1);
			}
		}
		if(!red_black_verify(tree))
		{
			puts("unverified");
			exit(1);
		}
	  	red_black_print(tree, stdout);
		
		char buff[256];
		fscanf(stdin, "%s", buff);
	}
  
  	red_black_print(tree, stdout);

  	red_black_del(tree);

  	return 0;
}
