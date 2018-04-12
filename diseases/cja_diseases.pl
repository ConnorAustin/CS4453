:- dynamic(known/1).

% Function that goes through process of diagnosing a disease
diagnose(X,Y):-
    disease(X,Y);
    not(known(fever(X,Y))),      ask_fever(X,A),      disease(X,Y);
    not(known(headache(X,Y))),   ask_headache(X,B),   disease(X,Y);
    not(known(pains(X,Y))),      ask_pains(X,C),      disease(X,Y);
    not(known(fatigue(X,Y))),    ask_fatigue(X,D),    disease(X,Y);
    not(known(exhaustion(X,Y))), ask_exhaustion(X,E), disease(X,Y);
    not(known(stuffyNose(X,Y))), ask_stuffyNose(X,F), disease(X,Y);
    not(known(sneezing(X,Y))),   ask_sneezing(X,G),   disease(X,Y);
    not(known(soreThroat(X,Y))), ask_soreThroat(X,H), disease(X,Y);
    not(known(cough(X,Y))),      ask_cough(X,I),      disease(X,Y);
    not(known(wateryEye(X,Y))),  ask_wateryEye(X,J),  disease(X,Y).

disease(X, flu):-
    fever(X, high),
    headache(X, prominent),
    pains(X, severe),
    fatigue(X, severe),
    exhaustion(X, prominent),
    stuffyNose(X, sometimes),
    sneezing(X, sometimes),
    soreThroat(X, sometimes),
    cough(X, severe),
    wateryEye(X, none).

disease(X, cold):-
    fever(X, rare),
    headache(X, rare),
    pains(X, slight),
    fatigue(X, mild),
    exhaustion(X, none),
    stuffyNose(X, common),
    sneezing(X, usual),
    soreThroat(X, common),
    cough(X, mild),
    wateryEye(X, rare).

disease(X, allergy):-
    fever(X, never),
    headache(X, never),
    pains(X, never),
    fatigue(X, mild),
    exhaustion(X, none),
    stuffyNose(X, common),
    sneezing(X, none),
    soreThroat(X, sometimes),
    cough(X, sometimes),
    wateryEye(X, common).

disease(X, ebola):-
    fever(X, severe),
    headache(X, severe),
    pains(X, severe),
    fatigue(X, severe),
    exhaustion(X, severe),
    stuffyNose(X, none),
    sneezing(X, none),
    soreThroat(X, none),
    cough(X, none),
    wateryEye(X, none).

disease(X, bubonicPlague):-
    fever(X, high),
    headache(X, high),
    pains(X, high),
    fatigue(X, high),
    exhaustion(X, high),
    stuffyNose(X, none),
    sneezing(X, none),
    soreThroat(X, none),
    cough(X, none),
    wateryEye(X, none).

ask_fever(X, Y):-
    write('What kind of fever does '),
    write(X),
    write(' have?'),
    nl,
    write('(never, rare, high, severe)'),
    nl, nl,
    read(Y),
    asserta(known(fever(X, Y))).

ask_headache(X, Y):-
    write('What kind of headache does '),
    write(X),
    write(' have?'),
    nl,
    write('(never, rare, prominent, high, severe)'),
    nl, nl,
    read(Y),
    asserta(known(headache(X, Y))).

ask_pains(X, Y):-
    write('What kind of pain does '),
    write(X),
    write(' have?'),
    nl,
    write('(never, slight, high, severe)'),
    nl, nl,
    read(Y),
    asserta(known(pains(X, Y))).

ask_fatigue(X, Y):-
    write('What kind of fatigue does '),
    write(X),
    write(' have?'),
    nl,
    write('(mild, high, severe)'),
    nl, nl,
    read(Y),
    asserta(known(fatigue(X, Y))).

ask_exhaustion(X, Y):-
    write('What kind of exhaustion does '),
    write(X),
    write(' have?'),
    nl,
    write('(none, prominent, high, severe)'),
    nl, nl,
    read(Y),
    asserta(known(exhaustion(X, Y))).

ask_stuffyNose(X, Y):-
    write('What kind of stuffy nose does '),
    write(X),
    write(' have?'),
    nl,
    write('(none, sometimes, common)'),
    nl, nl,
    read(Y),
    asserta(known(stuffyNose(X, Y))).

ask_sneezing(X, Y):-
    write('What kind of sneezing does '),
    write(X),
    write(' have?'),
    nl,
    write('(none, sometimes, usual)'),
    nl, nl,
    read(Y),
    asserta(known(sneezing(X, Y))).

ask_soreThroat(X, Y):-
    write('What kind of sore throat does '),
    write(X),
    write(' have?'),
    nl,
    write('(none, sometimes, common)'),
    nl, nl,
    read(Y),
    asserta(known(soreThroat(X, Y))).

ask_cough(X, Y):-
    write('What kind of cough does '),
    write(X),
    write(' have?'),
    nl,
    write('(none, sometimes, mild, severe)'),
    nl, nl,
    read(Y),
    asserta(known(cough(X, Y))).

ask_wateryEye(X, Y):-
    write('What kind watery eyes does '),
    write(X),
    write(' have?'),
    nl,
    write('(none, rare, common)'),
    nl, nl,
    read(Y),
    asserta(known(wateryEye(X, Y))).

fever(X, Y):-      known(fever(X, Y)).
headache(X, Y):-   known(headache(X, Y)).
pains(X, Y):-      known(pains(X, Y)).
fatigue(X, Y):-    known(fatigue(X, Y)).
exhaustion(X, Y):- known(exhaustion(X, Y)).
stuffyNose(X, Y):- known(stuffyNose(X, Y)).
sneezing(X, Y):-   known(sneezing(X, Y)).
soreThroat(X, Y):- known(soreThroat(X, Y)).
cough(X, Y):-      known(cough(X, Y)).
wateryEye(X, Y):-  known(wateryEye(X, Y)).