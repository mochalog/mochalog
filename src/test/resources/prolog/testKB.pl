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


data(27).
data(2.3333).
data(tea).
data("this is a native string").
data('this is quoted string').
data(mother(john,father(peter,mark),23.222)).
data([]).
data([1,2,3,4]).
data([peter, mother(maria), 34, [1,2,3,4]]).
data(_Variable).


data_string("string0").
data_string(atom0).