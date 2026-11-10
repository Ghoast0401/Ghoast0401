package com.Tabetomo.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.Tabetomo.Review.ReviewService;

// 管理画面(admin_dashboard.html)のレビュー管理タブから、
// 投稿されたレビューを承認/却下するためのエンドポイント。
// これが無いと ReviewEntity.status が PENDING のまま変わらず、
// ShopEntity.getAverageRating() / getReviewCount() が常に0になってしまう。
@Controller
public class ReviewApproval {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/admin/reviews/{id}/approve")
    public String approve(@PathVariable Long id) {
        reviewService.updateStatus(id, "APPROVED");
        return "redirect:/admin?activeTab=reviews";
    }

    @PostMapping("/admin/reviews/{id}/reject")
    public String reject(@PathVariable Long id) {
        reviewService.updateStatus(id, "REJECTED");
        return "redirect:/admin?activeTab=reviews";
    }
}

