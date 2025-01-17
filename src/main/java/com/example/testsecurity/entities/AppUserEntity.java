package com.example.testsecurity.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/*
    @author : Eton.lin
    @description TODO
    @date 2025-01-12 下午 01:47
*/
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class AppUserEntity implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String email;
    @Column()
    private String password;


    @Enumerated(EnumType.STRING)
    private AppUserRole userRole; // 使用者角色(ex: USER或ADMIN等)

    /*
      用於提供該用戶授權權限集合
      @return 用戶被授權的權限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }


    /*
      用來判斷使用者的帳戶是否過期
      @return 如果帳戶已過期，返回false，表示使用者不應該被授權，反之則true。
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*
      用來判斷使用者的帳戶是否被鎖定
      @return 如果帳戶被鎖定，返回false，表示使用者不應該被授權。
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*
      用來判斷使用者的認證信息是否過期，例如密碼是否過期
      @return 如果認證信息已過期，返回false，表示使用者不應該被授權。
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /*
      用來判斷使用者是否啟用，如果使用者已被禁用
      @return 返回false，表示使用者不應該被授權。
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AppUserEntity that = (AppUserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}