package linkopinghackers.swipeefy.cardstack;

import linkopinghackers.swipeefy.TabLayoutActivity.SwipeFragment;

/**
 * Created by Alexander on 2016-08-09.
 */
public class CardStackListener implements CardStack.CardEventListener{

    private SwipeFragment swipeFragment;
    private CardStack cardStack;
    private int currentIndex;
    public CardStackListener (SwipeFragment swipeFragment, CardStack cardStack){
        this.swipeFragment = swipeFragment;
        this.cardStack = cardStack;

    }

//implement card event interface
@Override
public boolean swipeEnd(int direction, float distance) {
        //if "return true" the dismiss animation will be triggered
        //if false, the card will move back to stack
        //distance is finger swipe distance in dp

        //the direction indicate swipe direction
        //there are four directions
        //  0  |  1
        // ----------
        //  2  |  3

        return (distance>300)? true : false;
        }

@Override
public boolean swipeStart(int direction, float distance) {

    currentIndex = cardStack.getCurrIndex();

        return true;
        }



@Override
public boolean swipeContinue(int direction, float distanceX, float distanceY) {

        return true;
        }

@Override
public void discarded(int id, int direction) {
    swipeFragment.onDiscard(currentIndex);

        //this callback invoked when dismiss animation is finished.
        }

@Override
public void topCardTapped() {
        //this callback invoked when a top card is tapped by user.
        }
        }