package javabot.operations.time;

public class Tri<T> {
    private static int OFFSET = 'a';
    private static int ALPHABET_SIZE = 26;
    Node<T> root;

    public Tri( ) {
        root = new Node( );
    }

    public T get( String key ) {

        if ( key == null ) {
            return null;
        }

        key = clean( key );

        Node current = root;
        for( int i = 0; i < key.length( ); i++ ) {
            int index = hash( key.charAt( i ) );
            Node node = current.getChild( index );

            if ( node == null ) {
                return null;
            }
            current = node;
        }
        return (T) current.getValue( );
    }

    public void insert( String key, T value ) {
        key = clean( key );

        Node current = root;
        for( int i = 0; i < key.length( ); i++ ) {
            int index = hash( key.charAt( i ) );
            Node node = current.getChild( index );

            if ( node == null ) {
                node = new Node( );
                current.setChild( index, node );
            }
            current = node;
        }
        current.setValue( value );
    }

    private String clean( String key ) {
        return key.toLowerCase().replaceAll("[^a-z]", "");
    }

    private int hash( char c ) {
        return c - OFFSET;
    }

    private char toChar( int index ) {
        return (char) ( index + OFFSET );
    }

    private class Node<T> {
        Node[] child = new Node[ ALPHABET_SIZE ];
        T value;

        public void setValue( T value ) {
            this.value = value;
        }

        public T getValue( ) {
            return value;
        }

        public Node getChild( int index ) {
            return child[ index ];
        }

        public void setChild( int index, Node node ) {
            child[ index ] = node;
        }
    }
}
