:- dynamic test/1, test/2, no_clause/1, test/1, test_plus/2, test_forever/1, person/3.

get_hello_world(hello).
get_hello_world(world).

test(80).
test(2).
test(4).
test(6).
test(8).

test_plus(N, R) :- 
	number(N),
	test(N2), R is N2 + N.
	
test_slow(X) :-
	test(X),
	sleep(0.5).

	
person(john, 20, melbourne).
person(maria, 31, sydney).
person(adam, 18, geelong).
person(michelle, 14, lorne).


% current_job(car, floor, direction)
current_job(0, 3, up).
current_job(1, 5, down).

% Contains various types of data types
data(27).
data(2.3333).
data(tea).
data("this is a native string").
data('this is quoted string').
data(mother(age(john,add(32,1)),father(peter,mark),23.222)).
data([]).
data([1,2,3,4]).
data([peter, mother(maria), 34, [1,2,3,4]]).
data(_Variable).



%%%%%%%%%%%%%%%%%%%%%%%%%
% TEST FOR STRINGS
%%%%%%%%%%%%%%%%%%%%%%%%%

% JPL will not see this string, as strings are converted into atoms
data_string("string0").


% Instead do this wrapper:
data_string_wrapper(X) :-
    (   atom(X)
    ->  atom_string(X, XString),
        data_string(XString)
    ;   var(X)
    ->  full_name(XString),
        atom_string(X, XString)
    ).


%%%%%%%%%%%%%%%%%%%%%%%%%
% TEST FOR JREF
%%%%%%%%%%%%%%%%%%%%%%%%%


%% Check what can you do from Prolog to call Java: http://www.swi-prolog.org/pldoc/man?section=jpl
print_integer(JRef, X) :-
%    jpl_get(JRef, intValue, X),         % this if it is accessing a field
    jpl_call(JRef, intValue, [], X),    % X should be the int value of object Integer JRef
    jpl_ref_to_type(JRef, T),           % T should be class([java,lang],[Integer])
    jpl_type_to_classname(T, ClassName),    % ClassName should be java.lang.Integer
    format(string(Text), "The integer value of JAVA object (~s) is ~d", [ClassName, X]),
    writeln(Text).




