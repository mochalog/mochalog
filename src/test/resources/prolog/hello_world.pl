/**
 * Copyright 2017 The Mochalog Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

:- dynamic no_clause/1.

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

test_forever(X) :- test(X).
test_forever(X) :- test_forever(X).


person(john, 20, melbourne).
person(maria, 31, sydney).
person(adam, 18, geelong).
person(michelle, 14, lorne).