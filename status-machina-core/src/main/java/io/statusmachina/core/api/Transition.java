/*
 *  Copyright 2019 <---> Present Status Machina Contributors (https://github.com/entzik/status-machina/graphs/contributors)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *  CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations under the License.
 *
 */

package io.statusmachina.core.api;

import java.util.Optional;

/**
 * Specifies if and how a state machine can transition from one state to another. It matches an edge in the
 * state machine diagram graph.
 * <p>
 * An action can be specified to be executed as part of the transition. If the action throws an exception, the transition
 * will not be cancelled and the machine will be put in an error state.
 * <p>
 * The transition action can interact with external systems and can mutate the machine's context.
 * <p>
 * transitions can be automatic ( STP: Straight Through Processing ) or event triggered
 *
 * @param <S> the state type
 * @param <E> the event type
 */
public class Transition<S, E> {
    private final S from;
    private final S to;
    private final Optional<E> event;
    private Optional<TransitionAction<?>> action;

    /**
     * Configure a transition so that if the machine is in a specified current state ("from") and receives the specified
     * event ("event"), it will move to the specified target state ("to") and execute the specified action in the process.
     *
     * @param from   the current state
     * @param to     the target state
     * @param event  the event that triggers the transition
     * @param action the action to be executed
     * @param <S>    the state type
     * @param <E>    the event type
     * @return a {@link Transition} instance
     */
    public static <S, E> Transition<S, E> event(S from, S to, E event, TransitionAction<?> action) {
        return new Transition<>(from, to, event, action);
    }

    /**
     * Configure a transition so that if the machine is in a specified current state ("from") and receives the specified
     * event ("event"), it will move to the specified target state ("to").
     *
     * @param from  the current state
     * @param to    the target state
     * @param event the event that triggers the transition
     * @param <S>   the state type
     * @param <E>   the event type
     * @return a {@link Transition} instance
     */
    public static <S, E> Transition<S, E> event(S from, S to, E event) {
        return new Transition<>(from, to, event);
    }

    /**
     * Configure a transition so that if the machine is in a specified current state ("from") it will automatically
     * move to the specified target state ("to") and execute the specified action in the process.
     *
     * @param from the current state
     * @param to   the target state
     * @param action the action to be executed
     * @param <S>  the state type
     * @param <E>  the event type
     * @return a {@link Transition} instance
     */
    public static <S, E> Transition<S, E> stp(S from, S to, TransitionAction<?> action) {
        return new Transition<>(from, to, action);
    }

    /**
     * Configure a transition so that if the machine is in a specified current state ("from") it will automatically
     * move to the specified target state ("to").
     *
     * @param from the current state
     * @param to   the target state
     * @param <S>  the state type
     * @param <E>  the event type
     * @return a {@link Transition} instance
     */
    public static <S, E> Transition<S, E> stp(S from, S to) {
        return new Transition<>(from, to);
    }

    private Transition(S from, S to, E event, TransitionAction<?> action) {
        this.from = from;
        this.to = to;
        this.event = Optional.of(event);
        this.action = Optional.of(action);
    }

    private Transition(S from, S to, E event) {
        this.from = from;
        this.to = to;
        this.event = Optional.of(event);
        this.action = Optional.empty();
    }

    private Transition(S from, S to, TransitionAction<?> action) {
        this.from = from;
        this.to = to;
        this.event = Optional.empty();
        this.action = Optional.of(action);
    }

    private Transition(S from, S to) {
        this.from = from;
        this.to = to;
        this.event = Optional.empty();
        this.action = Optional.empty();
    }

    /**
     * return the state in which the state machine needs to be in order for this transition to activate (if all other
     * conditions are fulfilled)
     *
     * @return the state
     */
    public S getFrom() {
        return from;
    }

    /**
     * return the state in which this transition will end up should this transition activate and cbe carried over successfully
     * @return the target state
     */
    public S getTo() {
        return to;
    }

    /**
     * returns the event that needs to be sent to the state machine in order to trigger this transition (should all other
     * conditions be fulfilled)
     *
     * @return the event
     */
    public Optional<E> getEvent() {
        return event;
    }

    /**
     * @return true if this is an STP action, false otherwise
     */
    public boolean isSTP() {
        return !event.isPresent();
    }

    /**
     * @return the action to be executed as part of the transition, should one be configured
     */
    public Optional<TransitionAction<?>> getAction() {
        return action;
    }
}
