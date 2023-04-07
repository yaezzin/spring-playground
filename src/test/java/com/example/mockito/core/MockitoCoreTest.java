package com.example.mockito.core;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Test
    public void matcher() {
        LinkedList mockedList = mock(LinkedList.class);

        when(mockedList.get(anyInt())).thenReturn("element");
        System.out.println(mockedList.get(999)); // element

        verify(mockedList).get(anyInt());
    }

    @Test
    public void matcher_argument() {
        /* all arguments have to be provided by matchers. */
        // verify(mock).someMethod(anyInt(), anyString(), eq("third argument"));
        // verify(mock).someMethod(anyInt(), anyString(), "third argument");
    }

    @Test
    public void do_throw() {
        LinkedList mockedList = mock(LinkedList.class);

        // When a method is called inside when(), it must have a return value. clear() returns nothing.
        // when(mockedList.clear()).thenThrow(new RuntimeException());
        doThrow(new RuntimeException()).when(mockedList).clear();

        assertThrows(RuntimeException.class, () -> mockedList.clear());
    }

    @Test
    public void order_single() {
        List singleMock = mock(List.class);

        singleMock.add("was added first");
        singleMock.add("was added second");

        InOrder inOrder = inOrder(singleMock);

        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");
    }

    @Test
    public void order_multiple() {
        // Multiple mocks that must be used in a particular order
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        firstMock.add("was called first");
        secondMock.add("was called second");

        InOrder inOrder = inOrder(firstMock, secondMock);

        inOrder.verify(firstMock).add("was called first");
        inOrder.verify(secondMock).add("was called second");
    }
}
