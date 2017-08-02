/*
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

package io.mochalog.util.collections;

import java.util.Collection;
import java.util.List;
import java.util.Comparator;
import java.util.function.UnaryOperator;

/**
 * Implementation of a Java List which is unmodifiable
 * (i.e. elements cannot be added, removed or moved)
 */
public abstract class UnmodifiableList<T> implements List<T>
{
    @Deprecated
    @Override
    public boolean add(T querySolution)
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public boolean remove(Object o)
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public boolean addAll(int index, Collection<? extends T> c)
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public boolean removeAll(Collection<?> c)
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public boolean retainAll(Collection<?> c)
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public void replaceAll(UnaryOperator<T> operator)
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public void sort(Comparator<? super T> c)
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public void clear()
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public T set(int index, T element)
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public void add(int index, T element)
    {
        throw getModificationUnsupportedException();
    }

    @Deprecated
    @Override
    public T remove(int index)
    {
        throw getModificationUnsupportedException();
    }

    /**
     * Get exception notifying that given list operation provokes
     * list moodification, which is illegal
     * @return Unsupported operation exception
     */
    private static UnsupportedOperationException getModificationUnsupportedException()
    {
        throw new UnsupportedOperationException("Illegal operation. Specified list is unmodifiable.");
    }
}
