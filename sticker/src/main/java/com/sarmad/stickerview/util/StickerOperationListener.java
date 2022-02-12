package com.sarmad.stickerview.util;

public interface StickerOperationListener {

    int IS_DRAGGING = 1321;
    int DRAGGING_STOPPED = 1322;


    void onStickerSelected(Sticker stickerView);
    void onStickerClosed(Sticker stickerView);
    void onStickerDragged(Sticker sticker,int state);
}
