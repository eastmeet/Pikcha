import React, { useEffect, useState } from "react";
import { MdModeEdit, MdDelete, MdPlace } from "react-icons/md";
import { RxDoubleArrowLeft } from "react-icons/rx";
import { AiFillHeart, AiFillEye, AiOutlineShareAlt } from "react-icons/ai";
import { FaRegCommentDots } from "react-icons/fa";
import PostComment from "../../components/Postcomment/PostComment";
import axios from "../../utils/axiosinstance";
import Button from "../../components/Button";
import { useNavigate, useParams, Link } from "react-router-dom";
import { useRecoilState } from "recoil";
import { LoginState, MemberId } from "../../recoil/state";
import Modal from "../../components/Modal";
import { Header } from "../../components/Header";
import Footer from "../../components/Footer";
import * as dp from "./DetailPostStyled";
import { useMediaQuery } from "react-responsive";
import MobileHeader from "../../components/Header/MobileHeader";
import { MenuSideBar, MenuButton } from "../MainResponsive";

export interface PostDetailType {
  attractionAddress: string;
  attractionId: number;
  attractionName: string;
  memberId: number;
  comments: [
    {
      commentId: number;
      memberId: number;
      username: string;
      memberPicture: string;
      commentContent: string;
      createdAt: string;
      modifiedAt: string;
    }
  ];
  createdAt: string;
  isVoted: boolean;
  likes: number;
  modifiedAt: string;
  picture: string;
  postContents: string[];
  postHashTags: string[];
  postId: number;
  postImageUrls: string[];
  postTitle: string;
  username: string;
  views: number;
}

export interface CommentType {
  commentId: number;
  memberId: number;
  username: string;
  memberPicture: string;
  commentContent: string;
  createdAt: string;
  modifiedAt: string;
}

export interface ArrayCommentType extends Array<CommentType> {}
// PostContent 리팩토링 예정
// interface PostContentsType {
//   imageURL: string;
//   content: string;
//   imageId: number;
// }
// interface ArrayPostCotentsType extends Array<PostContentsType> {}
// const [postContents, setPostContents] = useState<
//   ArrayPostCotentsType | PostContentsType
// >([]);

const DetailPost = () => {
  const [post, setPost] = useState<PostDetailType>();
  const [postComments, setPostComments] = useState<ArrayCommentType>();
  const [comment, setComment] = useState("");
  const [isLogin] = useRecoilState(LoginState);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const { id } = useParams();
  const [memberId] = useRecoilState(MemberId);
  const [isNavbarChecked, setIsNavbarChecked] = useState<boolean>(false);

  const [postId, setPostId] = useState<number>();
  const [isVoted, setIsVoted] = useState<boolean>();
  const navigate = useNavigate();
  const Mobile = useMediaQuery({
    query: "(max-width: 768px)",
  });


  useEffect(() => {
    if (isLogin) {
      axios
        .get(`/posts/details/${id}/${memberId}`)
        .then((res) => {
          setPost(res.data.data);
          const { comments, postId, isVoted } = res.data.data;
          setPostComments(comments);
          setPostId(postId);
          setIsVoted(isVoted);
        })
        .catch((err) => console.error(err));
    } else {
      axios
        .get(`/posts/details/${id}`)
        .then((res) => setPost(res.data.data))
        .catch((err) => console.error(err));
      setPostComments(post?.comments);
    }
  }, [isVoted]);

  const handleCommentSubmit = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    axios
      .post(`/comments/upload/${id}`, {
        commentContent: comment,
      })
      .then((res) => {
        if (res.status === 201) {
          setComment("");
          window.location.reload();
        }
      })
      .catch((err) => console.error(err));
  };
  let data: any[] = [];
  for (let i = 0; i < post?.postImageUrls.length!; i++) {
    data.push({
      imageURL: post?.postImageUrls[i],
      content: post?.postContents[i],
      imgageId: i + 1,
    });
  }
  const deleteHandler = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    if (window.confirm("정말 삭제하시겠습니까?")) {
      axios
        .delete(`/posts/delete/${id}`)
        .then((res) => {
          if (res.status === 204) {
            alert("삭제가 완료되었습니다.");
            navigate(-1);
          }
        })
        .catch((err) => console.log(err));
    }
  };

  const handleCommentWrite = () => {
    if (!isLogin) setIsModalVisible(true);
  };

  const handleCopyClipBoard = async (text: string) => {
    try {
      await navigator.clipboard.writeText(text);
      alert("url이 성공적으로 복사되었습니다.");
    } catch (err) {
      console.error(err);
    }
  };

  const handleLikePost = () => {
    if (!isLogin) setIsModalVisible(true);

    axios.post(`/posts/likes/${postId}`).then((res) => {
      setIsVoted(res.data.data.isVoted);
    });
  };

  return (
    <>
     { Mobile ? 
     <MobileHeader
     isNavbarChecked={isNavbarChecked}
     setIsNavbarChecked={setIsNavbarChecked}
     ></MobileHeader>:
     <Header>
        <Header.HeaderTop />
        <Header.HeaderBody />
      </Header>
      }
      {isNavbarChecked ? 
        <MenuSideBar>
          <Link to='/attractions'><MenuButton>명소</MenuButton></Link>
          <Link to='/posts'><MenuButton>포스트</MenuButton></Link>
          <Link to='/map'><MenuButton>내 주변 명소찾기</MenuButton></Link>
        </MenuSideBar> : null}

      <dp.DetailPostWrapper style = { Mobile ? { width: "100%" }: {margin:"30px auto"}}>
        {isModalVisible && <Modal setIsModalVisible={setIsModalVisible} />}
        {(post && post.memberId === memberId) || memberId === 1 ? (
          <dp.PostMangeButtnContainer>
            <dp.PostManageButton onClick={() => navigate(`/edit/${id}`)}>
              <MdModeEdit /> 수정
            </dp.PostManageButton>
            <dp.PostManageButton onClick={deleteHandler}>
              <MdDelete /> 삭제
            </dp.PostManageButton>
          </dp.PostMangeButtnContainer>
        ) : null}
        <dp.DetailPostTitle style = { Mobile ? { width : "100%", marginTop:"20px"} : {width : "80%"}  } >
          <h2>{post?.postTitle}</h2>
        </dp.DetailPostTitle>
       
        <dp.DetailPostInfo>
          <dp.DetailPostAttractionsContainer>
            {post?.attractionName}
            <p>
              <MdPlace /> &nbsp;{post?.attractionAddress}
            </p>
          </dp.DetailPostAttractionsContainer>

          <div style = { Mobile ? { width : "100%", marginTop:"25px", marginRight : "40px"}: {width : "80%"}  }>
            <button
              onClick={() =>
                navigate(`/attractions/detail/${post?.attractionId}`)
              }
            >
              <RxDoubleArrowLeft />
              &nbsp; 이 명소 포스트 더보기
            </button>
            <span>{post?.createdAt.slice(0, 10)}</span>
          </div>

        </dp.DetailPostInfo>

        <dp.PostContentContainer>
          <dp.PostContentBox>
            {data.map((post, idx) => (
              <div>
                <div>
                  <img src={post.imageURL} alt="picture" key={post.postId} />
                </div>
                <div>{post.content}</div>
              </div>
            ))}
          </dp.PostContentBox>
          <div>
            {post &&
              post?.postHashTags.map((tag, idx) => (
                <>
                  <dp.TagsButton key={post.postId}>{tag}</dp.TagsButton>
                </>
              ))}
          </div>
          <dp.PostContentBottom>
            <div>
              <img alt="userImg" src={post?.picture} />
              <strong>{post?.username}</strong>님의 포스트
            </div>
            <div>
              <div
                onClick={() => {
                  handleCopyClipBoard(document.location.href);
                }}
              >
                <AiOutlineShareAlt />
                <span>공유</span>
              </div>
              <div>
                <AiFillEye />
                <span>{post?.views}</span>
              </div>
              <div>
                <AiFillHeart
                  onClick={handleLikePost}
                  color={isVoted === true ? "red" : "grey"}
                />
                <span>{post?.likes}</span>
              </div>
            </div>
          </dp.PostContentBottom>
        </dp.PostContentContainer>
        {postComments && postComments.length === 0 ? (
          <dp.EmptyCommentContainer>
            <FaRegCommentDots />
            첫번째 댓글을 남겨주세요.
          </dp.EmptyCommentContainer>
        ) : (
          postComments?.map((comment) => (
            <PostComment key={comment.commentId} comment={comment} />
          ))
        )}
        <dp.AddComment isLogin={isLogin}>
          <h3>댓글 남기기</h3>
          <div>
            <img
              src={
                "https://drive.google.com/uc?id=1OmsgU1GLU9iUBYe9ruw_Uy1AcrN57n4g"
              }
              alt="userImg"
            />
            <textarea
              placeholder="댓글을 남겨주세요!"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              onClick={handleCommentWrite}
            />
            {isLogin ? (
              <Button
                type="violet"
                width="75px"
                height="30px"
                text="등록"
                onClick={(e) => handleCommentSubmit(e)}
              />
            ) : (
              <Button type="gray" width="80px" height="35px" text="등록" />
            )}
          </div>
        </dp.AddComment>
      </dp.DetailPostWrapper>
      <Footer />
    </>
  );
};

export default DetailPost;
