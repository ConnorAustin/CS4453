:- dynamic(known/1).

whatsnakeis(X,Y):-
    snake(X,Y);
    not(known(has_rattle(X,Y))), ask_rattle(X,Z), snake(X,Y);
    not(known(size(X,Y))), ask_size(X,U), snake(X,Y);
    not(known(dorsal_pattern(X,Y))), ask_dorsal(X,W), snake(X,Y).

snake(X,pygmyRattler):-
       has_rattle(X,yes),
       size(X,small),
       dorsal_pattern(X,none).

snake(X,timberRattler):-
       has_rattle(X,yes),
       size(X,small),
       dorsal_pattern(X,banded).

snake(X,diamondBackRattler):-
       has_rattle(X,yes),
       size(X,large),
       dorsal_pattern(X,diamonds).

snake(X,easternMilk):-
       has_rattle(X,no),
       size(X,medium),
       dorsal_pattern(X,blotched).

snake(X,brownWater):-
       has_rattle(X,no),
       size(X,large),
       dorsal_pattern(X,banded).

snake(X,rainbow):-
       has_rattle(X,no),
       size(X,large),
       dorsal_pattern(X,stripped).

has_rattle(X,Y):- known(has_rattle(X,Y)).

size(X,Y):- known(size(X,Y)).

dorsal_pattern(X,Y):- known(dorsal_pattern(X,Y)).


ask_rattle(X,Y):-
       write('does '),
       write(X),
       write(' have a rattle (yes or no)?'),
       nl, nl,
       read(Y),
       asserta(known(has_rattle(X,Y))).

ask_size(X,Y):-
       write('what size is '),
       write(X),
       write('(large, small, medium)?'),
       nl, nl,
       read(Y),
       asserta(known(size(X,Y))).

ask_dorsal(X,Y):-
       write('what dorsal patern does '),
       write(X),
       write(' have ?'),
       nl, nl,
       read(Y),
       asserta(known(dorsal_pattern(X,Y))).












