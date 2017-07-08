NAME=remoteserver
CC=gcc
CFLAGS=-Wall -pthread -O2
LIBS=-lpigpio -lrt
ODIR=obj
SDIR=src

_DEPS=motordriver.h remoteserver.h
DEPS=$(patsubst %,$(SDIR)/%,$(_DEPS))

_OBJ=motordriver.o remoteserver.o
OBJ=$(patsubst %,$(ODIR)/%,$(_OBJ))

$(ODIR)/%.o: src/%.c $(DEPS)
	@mkdir -p $(@D)
	$(CC) -c -o $@ $< $(CFLAGS)

$(NAME): $(OBJ)
	$(CC) -o $@ $^ $(CFLAGS) $(LIBS)

.PHONY:	run

run:	$(NAME)
	sudo ./$(NAME)

.PHONY:	clean

clean:	
	rm $(NAME) $(ODIR)/*.o


