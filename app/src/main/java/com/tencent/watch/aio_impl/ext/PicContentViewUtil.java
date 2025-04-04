package com.tencent.watch.aio_impl.ext;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;

import com.tencent.mobileqq.aio.msglist.holder.base.PicSize;
import com.tencent.qqnt.kernel.nativeinterface.MsgElement;
import com.tencent.watch.aio_impl.data.AbsWatchRichMsgItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class PicContentViewUtil {
    @NotNull
    public static final PicContentViewUtil a = new PicContentViewUtil();

    /**
     * applyPicContentView
     */
    public void a(@Nullable LifecycleOwner lifecycleOwner,
    @NotNull AppCompatImageView picView,
    @NotNull AbsWatchRichMsgItem msgItem,
    @NotNull MsgElement picMsgElement,
    @NotNull Runnable clickCallback,
    @NotNull Function2<? super Boolean, ? super PicContentViewUtil$FailReason, Unit> onLoadResult
    ) {}

    /**
     * loadImage
     */
    public final void b(@Nullable LifecycleOwner lifecycleOwner,
                        @NotNull AppCompatImageView picView,
                        @NotNull String imagePath,
                        @NotNull PicSize picSize,
                        @NotNull MsgElement msgElement,
                        @NotNull AbsWatchRichMsgItem msgItem,
                        @NotNull PicContentViewUtil$LoadingImage loadingImage,
                        @Nullable Function2<? super Boolean, ? super PicContentViewUtil$FailReason, Unit> onLoadResult
    ) {
    }

    /**
     * loadLocalImage
     */
    public final void c(@Nullable LifecycleOwner lifecycleOwner,
                        @NotNull AbsWatchRichMsgItem msgItem,
                        @NotNull MsgElement picMsgElement,
                        @NotNull AppCompatImageView picView,
                        @NotNull String imagePath,
                        @NotNull PicSize picSize,
                        @NotNull PicContentViewUtil$LoadingImage loadingImage
    ) {
    }
}
