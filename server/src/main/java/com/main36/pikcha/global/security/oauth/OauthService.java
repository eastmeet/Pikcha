package com.main36.pikcha.global.security.oauth;

import com.main36.pikcha.domain.member.entity.Member;
import com.main36.pikcha.domain.member.repository.MemberRepository;
import com.main36.pikcha.global.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OauthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    private final CustomAuthorityUtils customAuthorityUtils;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // OAuth 서비스(github, google, naver)에서 가져온 유저 정보를 담고있음
        log.info("oAuth2User = {} ", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();  // OAuth 서비스 이름(ex. github, naver, google)
        log.info("registrationId = {} ", registrationId); // google

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // OAuth 로그인 시 키(pk)가 되는 값
        log.info("userNameAttributeName = {} ", userNameAttributeName); // sub

        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("attributes = {} ", attributes);
//{sub=106028432914978568484, name=DongWoo Lee, given_name=DongWoo, family_name=Lee, picture=https://lh3.googleusercontent.com/a/AEdFTp68KaSr161YqBFVS7fIB0VeLhvFWsXJUU8Mm_nxfA=s96-c, email=ys932184@gmail.com, email_verified=true, locale=ko}
        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes); // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌
        log.info("userProfile = {} ", userProfile);
        Member member = saveOrUpdate(userProfile);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(customAuthorityUtils.createRoles(member.getEmail()).toString())),
                attributes,
                userNameAttributeName);

    }


    private Member saveOrUpdate(UserProfile userProfile) {
        Member member = memberRepository.findByOauthId(userProfile.getOauthId())
                .map(m -> m.update(userProfile.getName(), userProfile.getEmail(), userProfile.getImageUrl())) // OAuth 서비스 사이트에서 유저 정보 변경이 있을 수 있기 때문에 우리 DB에도 update
                .orElse(userProfile.toMember(userProfile.getOauthId(), userProfile.getName(), userProfile.getEmail(), userProfile.getImageUrl(), customAuthorityUtils.createRoles(userProfile.getEmail())));
        return memberRepository.save(member);
    }
}
