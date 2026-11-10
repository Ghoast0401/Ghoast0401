package com.Tabetomo.Account;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByUserNameAndPassword(String userName,String  password);

	Optional <AccountEntity>findByUserName(String userName);

	// 管理画面のユーザー検索用（ユーザー名のあいまい検索）
	List<AccountEntity> findByUserNameContainingIgnoreCase(String keyword);

	// 管理画面のユーザー検索用（ユーザー名のあいまい一致 または IDの完全一致）
	@Query("SELECT a FROM AccountEntity a WHERE " +
	       "LOWER(a.userName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
	       "OR CAST(a.userId AS string) = :keyword")
	List<AccountEntity> searchByUserNameOrId(@Param("keyword") String keyword);

}