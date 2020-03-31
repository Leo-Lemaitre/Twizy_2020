clear all
close all
clc

%% traitement image twizy %%

%affichage des images

[A,mapA]= imread('boat.png');
% figure(1);
% imshow(A,mapA);
[B,mapB]= imread('elaine.jpg');
% figure(2);
% imshow(B,mapB);


%extraction module et phase de A 
A2=im2double(A);
A2fft = fftshift(fft2(A2));
modA = log10(abs(A2fft));
% figure;
% % imshow(modA/max(max(modA)));
% figure;
phaseA = angle(A2fft);
% imshow(phaseA);

%extraction module et phase de B
B2=im2double(B);
B2fft = fftshift(fft2(B2));
modB = log10(abs(B2fft));
% figure;
% imshow(modB/max(max(modB)));
% figure;
phaseB = angle(B2fft);
% imshow(phaseB);


%intervertir phase et module
modA = abs(A2fft);
A2bis = modA.*exp(i*phaseB);
A2bis = ifft2(A2bis);
% figure;
% imshow(A2bis/max(max(A2bis)));


modB = abs(B2fft);
B2bis = modB.*exp(i*phaseA);
B2bis = ifft2(B2bis);
% figure;
% imshow(B2bis/max(max(B2bis)));


imAse = A(1:2:end,1:2:end);
% figure;
% imshow(imAse);

imBse = B(1:2:end,1:2:end);
% figure;
% imshow(imBse);

h = [1 2 1]'*[1 2 1]/16;
imfA = imfilter(A,h);
imAse2 = imfA(1:2:end,1:2:end);
% figure;
% imshow(imAse2);

imfB = imfilter(B,h);
imBse2 = imfB(1:2:end,1:2:end);
% figure;
% imshow(imBse2);

%% seuillage d'image

%choix manuel
% figure; imhist(A);
seuilA = 80;
indsupA = A>seuilA;
indinfA = A<seuilA;
ANB = zeros(size(A));
ANB(indsupA) = 1;
% imshow(ANB);

%choix manuel
% figure; imhist(B);
seuilB = 130;
indsupB = B>seuilB;
indinfB = B<seuilB;
BNB = zeros(size(B));
BNB(indsupB) = 1;
% imshow(BNB);

%choix otsu
% figure;
seuilA2 = (graythresh(A)*size(imhist(A)));
seuilA2=seuilA2(1);
indsupA2 = A>seuilA2;
indinfA2 = A<seuilA2;
ANB2 = zeros(size(A));
ANB2(indsupA2) = 1;
% imshow(ANB2);

%choix otsu
figure;
seuilB2 = (graythresh(B)*size(imhist(B)));
seuilB2=seuilB2(1);
indsupB2 = B>seuilB2;
indinfB2 = B<seuilB2;
BNB2 = zeros(size(B));
BNB2(indsupB2) = 1;
% imshow(BNB2);


%hsv

Ipions = imread('pions.jpg');
Ipions = im2double(Ipions);
IpionsHSV = rgb2hsv(Ipions);
figure; imhist(IpionsHSV(:,:,1));
seuilJ = find((IpionsHSV(:,:,1)>0.12)&(IpionsHSV(:,:,1)<0.17));

%masque
seuilR = find((IpionsHSV(:,:,1)>0.97)&(IpionsHSV(:,:,1)<0.99));

Ipions1 = Ipions(:,:,1);
Ipions2 = Ipions(:,:,2);
Ipions3 = Ipions(:,:,3);

Ipionsseuil1 = zeros(size(Ipions,1));
Ipionsseuil2 = zeros(size(Ipions,1));
Ipionsseuil3 = zeros(size(Ipions,1));

Ipionsseuil1(seuilR)=Ipions1(seuilR);
Ipionsseuil2(seuilR)=Ipions2(seuilR);
Ipionsseuil3(seuilR)=Ipions3(seuilR);

Ipionsseuil(:,:,1)= Ipionsseuil1;
Ipionsseuil(:,:,2)= Ipionsseuil2;
Ipionsseuil(:,:,3)= Ipionsseuil3;

figure, imshow(Ipionsseuil);
% Phsv = rgb2hsv(P);
% Ph = Phsv(:,:,1);
% figure;
% imhist(Ph)
% % % imshow(Ph);
% seuilBlanc=0.7;
% for i=1:size(Ph,1)
%     for j=1:size(Ph,2)
%         if Ph(i,j)<seuilBlanc
%             Ph(i,j)=0;
%         end
%     end
% end
% Phsv(:,:,1)=Ph;
% PhRGB=hsv2rgb(Phsv);
% imshow(PhRGB);

% masque des pions rouge
mIpionsRouge = zeros(size(Ipions,1));
mIpionsRouge(seuilR) = 1;
mIpionsRouge(seuilJ) = 1;
figure,imshow(mIpionsRouge);
% ------------------------

SE = strel('disk',13);
erodedIpions = imerode(mIpionsRouge,SE);
% figure, imshow(erodedIpions);

SE2= strel('disk',15);
dilatedIpions = imdilate(erodedIpions,SE2);
% figure, imshow(dilatedIpions);

%% contour
% BWdilatedIpions = edge(dilatedIpions, 'Canny');
% figure, imshow(BWdilatedIpions);
% title('Contour de pions avec Canny');
% hold on
% SE3 = strel('disk',3);
% erodedElaine = imerode(B,SE3);
% dilatedElaine = imdilate(erodedElaine,SE3);
% BWellen = edge(dilatedElaine,'Prewitt');
% figure, imshow(BWellen);

% SE3 = strel('diamond',4);
% erodedBoat = imerode(A,SE3);
% dilatedBoat = imdilate(erodedBoat,SE3);
% BWboat = edge(dilatedBoat,'Sobel');
% % figure, imshow(A);
% figure, imshow(BWboat);

% [centers, radii, metric] = imfindcircles(BWdilatedIpions,[50 150],'Sensitivity',0.96);
% viscircles(centers, radii, 'LineStyle', '-');

%% Template matching 
% 
% alphabet = rgb2gray(imread('Alphabet.jpg'));
% letterU = imread('LettreU.jpg');
% BigLetterU = zeros(size(alphabet));
% BigLetterU(1:size(letterU,1),1:size(letterU,2)) = letterU;
% c = normxcorr2(alphabet,BigLetterU);
% figure, surf(c), shading flat
% 
% [ypeak, xpeak] = find(c==max(c(:)));
% yoffSet = gather(ypeak-size(letterU,1));
% xoffSet = gather(xpeak-size(letterU,2));
% figure,
% imshow(alphabet);
% imrect(gca, [xoffSet+1, yoffSet+1, size(letterU,2), size(letterU,1)]);

%% stan matching
stan = imread('StanislasFace.jpg');
stan = imresize(stan,0.585);

figure;
corners = detectSURFFeatures(rgb2gray(stan));
[features, valid_corners] = extractFeatures(rgb2gray(stan), corners);

imshow(stan); hold on
plot(valid_corners);

stan2 = imread('StanislasFace2.jpg');
figure;
corners2 = detectSURFFeatures(rgb2gray(stan2));
[features2, valid_corners2] = extractFeatures(rgb2gray(stan2), corners2);
imshow(stan2); hold on
plot(valid_corners2);

indexPairs = matchFeatures(features,features2);
matchedPoints1 = valid_corners(indexPairs(:,1),:);
matchedPoints2 = valid_corners2(indexPairs(:,2),:);

figure;ax = axes; showMatchedFeatures(rgb2gray(stan),rgb2gray(stan2),matchedPoints1,matchedPoints2,'montage','Parent',ax);






